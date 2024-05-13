package CoinMerge.coinMergeSpring.member.dto;

import CoinMerge.coinMergeSpring.member.domain.entity.Member;
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

  public static MemberResponse toMemberResponse(Member member) {
    return MemberResponse.builder().id(member.getId()).email(member.getEmail())
        .nickname(member.getNickname()).description(member.getDescription())
        .profileImageUri(member.getProfileImageUri()).build();
  }

}
