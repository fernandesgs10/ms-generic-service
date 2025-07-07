package br.com.generic.service.controller;

import br.com.generic.service.dto.AccountPaymentDto;
import br.com.generic.service.dto.ResponseTemplateDto;
import br.com.generic.service.mapper.AccountPaymentMapper;
import br.com.generic.service.service.AccountPaymentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("v1/account-payment")
@AllArgsConstructor
public class AccountPaymentController extends AbstractController<AccountPaymentDto> {

    private final AccountPaymentMapper accountPaymentMapper;
    private final AccountPaymentService accountPaymentService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @ApiOperation(value = "Save account payment", authorizations = {@Authorization(value = "OAuth2")})
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> save(@Valid @RequestBody AccountPaymentDto accountPaymentDto) {
        log.info("Saving account payment: {}", accountPaymentDto);

        var accountPaymentEntity = accountPaymentMapper.converterDtoObjectToEntity(accountPaymentDto);
        accountPaymentEntity.setNmCreated(getAuthentication().getName());

        var resultDto = accountPaymentMapper.converterEntityObjectToDto(accountPaymentService.save(accountPaymentEntity));

        var response = ResponseTemplateDto.createResponse(resultDto, HttpStatus.CREATED);
        log.info("Account payment saved successfully: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @ApiOperation(value = "Update account payment", authorizations = {@Authorization(value = "OAuth2")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> update(@Valid @RequestBody AccountPaymentDto accountPaymentDto) {
        log.info("Updating account payment: {}", accountPaymentDto);

        var accountPaymentEntity = accountPaymentMapper.converterDtoObjectToEntity(accountPaymentDto);
        accountPaymentEntity.setNmEdited(getAuthentication().getName());

        var resultDto = accountPaymentMapper.converterEntityObjectToDto(accountPaymentService.update(accountPaymentEntity));

        var response = ResponseTemplateDto.createResponse(resultDto, HttpStatus.OK);
        log.info("Account payment updated successfully: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/remove/id/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @ApiOperation(value = "Delete account payment", authorizations = {@Authorization(value = "OAuth2")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> remove(@PathVariable("id") long id) {
        log.info("Removing account payment with id: {}", id);

        var response = ResponseTemplateDto.createResponse(accountPaymentService.remove(id), HttpStatus.OK);
        log.info("Account payment removed successfully: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        log.info("Retrieving account payment by id: {}", id);

        var accountPaymentDto = accountPaymentMapper.converterEntityObjectToDto(accountPaymentService.findUserById(id));
        var response = ResponseTemplateDto.createResponse(accountPaymentDto, HttpStatus.OK);
        log.info("Account payment retrieved successfully: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @ApiOperation(value = "List account payments", authorizations = {@Authorization(value = "OAuth2")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully listed"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> findAll(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving list of account payments: {}", pageable);

        var accountPaymentEntities = accountPaymentService.findAll(pageable);
        var resultPage = convertEntityPage(accountPaymentEntities,
                accountEntity -> accountPaymentMapper.getModelMapper().map(accountEntity, AccountPaymentDto.class));

        var response = ResponseTemplateDto.createResponse(resultPage, HttpStatus.OK);
        log.info("Account payments retrieved successfully: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/dueDate/{dueDate}/description/{description}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @ApiOperation(value = "List account payments by due date and description", authorizations = {@Authorization(value = "OAuth2")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully listed"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> findByDueDateAndDescription(
            @PathVariable("dueDate") LocalDate dueDate,
            @PathVariable("description") String description,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving account payments by due date: {} and description: {}", dueDate, description);

        var accountPaymentEntities = accountPaymentService.findByDueDateAndDescription(dueDate, description, pageable);
        var resultPage = convertEntityPage(accountPaymentEntities,
                accountEntity -> accountPaymentMapper.getModelMapper().map(accountEntity, AccountPaymentDto.class));

        var response = ResponseTemplateDto.createResponse(resultPage, HttpStatus.OK);
        log.info("Account payments retrieved successfully: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/id/{id}/status/{status}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @ApiOperation(value = "Update account payment status", authorizations = {@Authorization(value = "OAuth2")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long id, @PathVariable("status") boolean status) {
        log.info("Updating status of account payment id: {} to {}", id, status);

        var accountPaymentEntity = accountPaymentService.findUserById(id);
        accountPaymentEntity.setNmEdited(getAuthentication().getName());
        accountPaymentEntity.setPaymentDate(status ? LocalDate.now() : null);
        accountPaymentEntity.setStatus(status);

        var resultDto = accountPaymentMapper.converterEntityObjectToDto(accountPaymentService.update(accountPaymentEntity));

        var response = ResponseTemplateDto.createResponse(resultDto, HttpStatus.OK);
        log.info("Account payment status updated successfully: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/paymentDateInit/{paymentDateInit}/paymentDateLimit/{paymentDateLimit}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @ApiOperation(value = "Sum amounts by payment date range", authorizations = {@Authorization(value = "OAuth2")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> findSumAmountByPaymentDate(
            @PathVariable("paymentDateInit") LocalDate paymentDateInit,
            @PathVariable("paymentDateLimit") LocalDate paymentDateLimit) {
        log.info("Retrieving sum of amounts between payment dates: {} and {}", paymentDateInit, paymentDateLimit);

        var sumAmount = accountPaymentService.findSumAmountByPaymentDate(paymentDateInit, paymentDateLimit);
        var response = ResponseTemplateDto.createResponse(sumAmount, HttpStatus.OK);
        log.info("Sum of amounts retrieved successfully: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/upload/csv")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @ApiOperation(value = "Upload file", authorizations = {@Authorization(value = "OAuth2")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully uploaded"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        log.info("Uploading file: {}", file.getOriginalFilename());

        var resultDto = accountPaymentService.uploadCsv(file,  getAuthentication().getName());
        var response = ResponseTemplateDto.createResponse(resultDto, HttpStatus.OK);
        log.info("File uploaded successfully: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
