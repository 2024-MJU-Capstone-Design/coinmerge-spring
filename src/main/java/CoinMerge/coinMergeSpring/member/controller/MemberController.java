package CoinMerge.coinMergeSpring.member.controller;

import CoinMerge.coinMergeSpring.common.annotation.LoginRequired;
import CoinMerge.coinMergeSpring.common.utils.SessionUtil;
import CoinMerge.coinMergeSpring.member.dto.LoginRequest;
import CoinMerge.coinMergeSpring.member.dto.LoginResponse;
import CoinMerge.coinMergeSpring.member.dto.MemberDto;
import CoinMerge.coinMergeSpring.member.dto.MemberResponse;
import CoinMerge.coinMergeSpring.member.dto.MemberUpdateRequest;
import CoinMerge.coinMergeSpring.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

  @PostMapping("/member")
  public ResponseEntity<MemberResponse> regist(@RequestBody @Valid final MemberDto memberDto)
      throws Exception {
    final MemberResponse response = memberService.signUp(memberDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid final LoginRequest loginRequest,
      HttpServletRequest httpServletRequest)
      throws Exception {
    final String memberId = memberService.login(loginRequest);
    final HttpSession session = httpServletRequest.getSession();

    session.setAttribute("MEMBER_ID", memberId);
    session.setMaxInactiveInterval(3600);

    return ResponseEntity.ok().build();
  }

  @LoginRequired
  @GetMapping("/logout")
  public ResponseEntity logout(HttpServletRequest httpServletRequest) {
    final HttpSession session = httpServletRequest.getSession();
    SessionUtil.logout(session);

    return ResponseEntity.ok().build();
  }

  @GetMapping("check/nickname")
  public ResponseEntity checkNickname(HttpServletRequest httpServletRequest) {
    final String nickname = httpServletRequest.getParameter("nickname");
    final boolean isValidNickname = memberService.checkNickname(nickname);

    return ResponseEntity.ok().build();
  }

  @LoginRequired
  @DeleteMapping("/member")
  public ResponseEntity unregist(HttpServletRequest httpServletRequest) {
    HttpSession session = httpServletRequest.getSession();
    String memberId = SessionUtil.getMemberId(session);
    memberService.unregist(memberId);

    return ResponseEntity.ok().build();
  }

  @LoginRequired
  @PutMapping("/member")
  public ResponseEntity<MemberResponse> updateMember(HttpServletRequest httpServletRequest,
      @RequestBody @Valid final
      MemberUpdateRequest memberUpdateRequest) throws Exception {
    HttpSession session = httpServletRequest.getSession();
    String memberId = SessionUtil.getMemberId(session);

    return ResponseEntity.status(HttpStatus.OK).body((memberService.updateMember(memberId, memberUpdateRequest)));
  }

  @LoginRequired
  @GetMapping("/member")
  public ResponseEntity<MemberResponse> getMember(HttpServletRequest httpServletRequest) {
    HttpSession session = httpServletRequest.getSession();
    String memberId = SessionUtil.getMemberId(session);

    return ResponseEntity.ok(memberService.getMember(memberId));
  }
}
