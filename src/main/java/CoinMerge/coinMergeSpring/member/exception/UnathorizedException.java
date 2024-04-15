package CoinMerge.coinMergeSpring.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UnathorizedException extends RuntimeException {
  private MemberErrorResult memberErrorResult;
}