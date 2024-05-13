package CoinMerge.coinMergeSpring.member.service;

import static CoinMerge.coinMergeSpring.common.utils.EncryptUtil.generateSHA256HashWithSalt;
import static CoinMerge.coinMergeSpring.common.utils.EncryptUtil.generateSalt;

import CoinMerge.coinMergeSpring.common.utils.EncryptUtil;
import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import CoinMerge.coinMergeSpring.member.domain.repository.MemberRepository;
import CoinMerge.coinMergeSpring.member.dto.LoginRequest;
import CoinMerge.coinMergeSpring.member.dto.MemberDto;
import CoinMerge.coinMergeSpring.member.dto.MemberResponse;
import CoinMerge.coinMergeSpring.member.dto.MemberUpdateRequest;
import CoinMerge.coinMergeSpring.member.exception.DuplicateNicknameException;
import CoinMerge.coinMergeSpring.member.exception.MemberErrorResult;
import CoinMerge.coinMergeSpring.member.exception.MemberException;
import CoinMerge.coinMergeSpring.member.exception.UnathorizedException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  public MemberResponse signUp(final MemberDto memberDto) throws Exception {
    final Member existMember = memberRepository.findByEmail(memberDto.getEmail());

    if (existMember != null) {
      throw new MemberException(MemberErrorResult.DUPLICATE_MEMBER_REGISTER);
    }

    String salt = generateSalt();
    String hashedPassword = generateSHA256HashWithSalt(memberDto.getPassword(), salt);
    Member member = Member.builder().email(memberDto.getEmail()).password(hashedPassword).salt(salt)
        .description(memberDto.getDescription()).nickname(memberDto.getNickname())
        .build();
    Member resultEntity = memberRepository.save(member);

    return MemberResponse.builder().id(resultEntity.getId()).email(resultEntity.getEmail())
        .nickname(resultEntity.getNickname()).description(resultEntity.getDescription())
        .profileImageUri(resultEntity.getProfileImageUri()).build();
  }

  public String login(final LoginRequest loginRequest)
      throws Exception {
    // get salt
    final Member member = memberRepository.findByEmail(loginRequest.getEmail());

    String hashedPassword = generateSHA256HashWithSalt(loginRequest.getPassword(),
        member.getSalt());
    if (!(member.getPassword().equals(hashedPassword))) {
      throw new UnathorizedException();
    }

    return member.getId();
  }

  public Boolean checkNickname(final String nickname) {
    final Member member = memberRepository.findByNickname(nickname);

    if (member != null) {
      throw new DuplicateNicknameException(MemberErrorResult.DUPLICATE_NICKNAME);
    }

    return true;
  }

  public void unregist(final String memberId) {
    final Optional<Member> optionalMember = memberRepository.findById(memberId);
    final Member member = optionalMember.orElseThrow(
        () -> new MemberException(MemberErrorResult.MEMBER_NOT_FOUND));

    memberRepository.deleteById(member.getId());
  }

  public MemberResponse updateMember(final String memberId,
      final MemberUpdateRequest memberUpdateRequest)
      throws Exception {
    final Optional<Member> optionalMember = memberRepository.findById(memberId);
    final Member member = optionalMember.orElseThrow(
        () -> new MemberException(MemberErrorResult.MEMBER_NOT_FOUND));
    final String newPassword = EncryptUtil.generateSHA256HashWithSalt(
        memberUpdateRequest.getPassword(), member.getSalt());

    if (member.getPassword().equals(newPassword) == false) {
      member.setPassword(newPassword);
    }
    if (member.getDescription() == null
        || member.getDescription().equals(memberUpdateRequest.getDescription()) == false) {
      member.setDescription(memberUpdateRequest.getDescription());
    }
    if (member.getProfileImageUri() == null
        || memberUpdateRequest.getProfileImageUri().equals(member.getProfileImageUri()) == false) {
      member.setProfileImageUri(memberUpdateRequest.getProfileImageUri());
    }

    return MemberResponse.builder().id(member.getId()).email(member.getEmail())
        .nickname(member.getNickname()).description(member.getDescription())
        .profileImageUri(member.getProfileImageUri()).build();
  }

  public MemberResponse getMember(final String memberId) {
    final Optional<Member> optionalMember = memberRepository.findById(memberId);
    final Member member = optionalMember.orElseThrow(
        () -> new MemberException(MemberErrorResult.MEMBER_NOT_FOUND));

    return MemberResponse.toMemberResponse(member);
  }
}
