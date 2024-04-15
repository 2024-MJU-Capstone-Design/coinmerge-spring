package CoinMerge.coinMergeSpring.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@RequiredArgsConstructor
public enum MemberErrorResult {
  DUPLICATE_MEMBER_REGISTER(HttpStatus.BAD_REQUEST, "중복된 유저가 회원가입을 요청했습니다."),
  UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 에러가 발생했습니다."),
  PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "패스워드가 일치하지 않습니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "유효하지 않은 접근입니다."),
  ;
  private final HttpStatus httpStatus;
  private final String message;
}
