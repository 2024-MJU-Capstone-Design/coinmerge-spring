package CoinMerge.coinMergeSpring.common.advice;

import CoinMerge.coinMergeSpring.member.exception.DuplicateNicknameException;
import CoinMerge.coinMergeSpring.member.exception.MemberErrorResult;
import CoinMerge.coinMergeSpring.member.exception.MemberException;
import CoinMerge.coinMergeSpring.member.exception.UnathorizedException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    final List<String> errorList = ex.getBindingResult().getAllErrors().stream().map(
        DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

    log.warn("Invalid DTO Parameter errors : {}", errorList);
    return this.makeErrorResponseEntity(errorList.toString());
  }

  private ResponseEntity<Object> makeErrorResponseEntity(final String errorDescription) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), errorDescription));
  }

  @ExceptionHandler({MemberException.class, DuplicateNicknameException.class})
  public ResponseEntity<ErrorResponse> handleMemberApiExeption(final Exception exception) {
    if(exception instanceof DuplicateNicknameException) {
      log.warn("DuplicateNicknameException occured: ", exception);
      return this.makeErrorResponseEntity(MemberErrorResult.DUPLICATE_NICKNAME);
    }else {
      log.warn("MemberException occured: ", exception);
      return this.makeErrorResponseEntity(MemberErrorResult.DUPLICATE_MEMBER_REGISTER);
    }
  }

  @ExceptionHandler({UnathorizedException.class})
  public ResponseEntity<ErrorResponse> handleLoginExeption(final UnathorizedException exception) {
    log.warn("PasswordNotMatchedException occured: ", exception);
    return this.makeErrorResponseEntity(MemberErrorResult.UNAUTHORIZED);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
    log.warn("Exception occur: ", exception);
    return this.makeErrorResponseEntity(MemberErrorResult.UNKNOWN_EXCEPTION);
  }

  private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final MemberErrorResult errorResult) {
    return ResponseEntity.status(errorResult.getHttpStatus())
        .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
  }

  @Getter
  @RequiredArgsConstructor
  static class ErrorResponse {

    private final String code;
    private final String message;
  }
}
