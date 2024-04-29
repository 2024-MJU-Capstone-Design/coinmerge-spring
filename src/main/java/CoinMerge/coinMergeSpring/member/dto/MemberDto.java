package CoinMerge.coinMergeSpring.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MemberDto {

  @NotEmpty
  @Email(message = "유효한 이메일 형식이 아닙니다.", regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
  private final String email;

  @NotEmpty
  @Pattern(message = "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.",
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!~$%^&-+=()])(?=\\S+$).{8,16}$")
  private final String password;

  @NotNull
  @Length(min = 3, max = 12)
  private final String nickname;
  private final String profileImageUri;
  private final String description;

  @Override
  public String toString() {
    return "MemberDto{" +
        "email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", nickname='" + nickname + '\'' +
        ", profileImageUri='" + profileImageUri + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
