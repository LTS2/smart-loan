package com.ysmeta.smartfin.domain.auth.password;

import com.ysmeta.smartfin.common.AbstractBaseEntity;
import com.ysmeta.smartfin.domain.user.UserEntity;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 비밀번호 엔티티 클래스입니다.
 *
 * @author : ewjin
 * @version : 0.0.1
 * @since : 2024. 8. 8.
 */
@Getter
@SuperBuilder
@Entity(name = "PASSWORD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordEntity extends AbstractBaseEntity {

	/**
	 * 유저 아이디 외래키
	 * cascade = 유저가 생성, 삭제될 때 비밀번호도 함께 생성, 삭제되도록 설정
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", unique = true, nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private UserEntity user;

	/**
	 * SHA-256로 암호화된 비밀번호
	 */
	private String hashedPassword;

	/**
	 * 솔트값
	 */
	private String salt;

	// 엔티티를 DTO로 변환하는 메서드
	public static PasswordDto fromEntity(PasswordEntity passwordEntity) {
		PasswordDto passwordDto = new PasswordDto();
		passwordDto.setHashedPassword(passwordEntity.getHashedPassword());
		passwordDto.setSalt(passwordEntity.getSalt());

		// UserEntity를 UserResponseDto로 변환하여 설정
		// passwordDto.setUser(UserDto.CreateUserResponseDto.fromEntity(passwordEntity.getUser()));
		return passwordDto;
	}

	// DTO를 엔티티로 변환하는 메서드
	public static PasswordEntity fromDto(PasswordDto passwordDto, UserEntity userEntity) {
		return PasswordEntity.builder()
			.user(userEntity)
			.hashedPassword(passwordDto.getHashedPassword())
			.salt(passwordDto.getSalt())
			.build();
	}
}
