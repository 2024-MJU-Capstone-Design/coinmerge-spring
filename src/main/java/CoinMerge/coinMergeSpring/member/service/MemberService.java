package CoinMerge.coinMergeSpring.member.service;

import static CoinMerge.coinMergeSpring.common.utils.EncryptUtil.generateSHA256HashWithSalt;
import static CoinMerge.coinMergeSpring.common.utils.EncryptUtil.generateSalt;

import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import CoinMerge.coinMergeSpring.member.domain.repository.MemberRepository;
import CoinMerge.coinMergeSpring.member.dto.LoginRequest;
import CoinMerge.coinMergeSpring.member.dto.MemberDto;
import CoinMerge.coinMergeSpring.member.dto.MemberResponse;
import CoinMerge.coinMergeSpring.member.exception.MemberErrorResult;
import CoinMerge.coinMergeSpring.member.exception.MemberException;
import CoinMerge.coinMergeSpring.member.exception.UnathorizedException;
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

    return MemberResponse.builder().id(resultEntity.getId()).email(resultEntity.getEmail()).build();
  }

  public String login (final LoginRequest loginRequest)
      throws Exception {
    // get salt
    final Member member = memberRepository.findByEmail(loginRequest.getEmail());

    String hashedPassword = generateSHA256HashWithSalt(loginRequest.getPassword(), member.getSalt());
    if(!(member.getPassword().equals(hashedPassword))) {
      throw new UnathorizedException();
    }

    return member.getId();
  }
}
