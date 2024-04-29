package CoinMerge.coinMergeSpring.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MemberResponse {
  private final String id;
  private final String email;
  private final String description;
  private final String profileImageUri;
  private final String nickname;
}
