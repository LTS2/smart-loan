package com.ysmeta.smartfin.domain.user.entity;

import static jakarta.persistence.FetchType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ysmeta.smartfin.common.AbstractBaseEntity;
import com.ysmeta.smartfin.domain.password.PasswordEntity;
import com.ysmeta.smartfin.domain.user.dto.UserDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 고객 엔티티입니다.
 *
 * @author : ewjin
 * @since : 2024. 7. 29.
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "USER")
public class UserEntity extends AbstractBaseEntity {

	/**
	 * 이름
	 */
	@Column(nullable = false)
	String name;
	/**
	 * 이메일
	 */
	@Column(unique = true, nullable = false)
	String email;
	@Column(nullable = false)
	private String companyName;
	@OneToOne(mappedBy = "user")
	private PasswordEntity passwordEntity;
	@OneToMany(mappedBy = "user", fetch = LAZY)
	private List<UserRoleEntity> userRoles = new ArrayList<>();

	public static UserEntity fromEmail(String email) {
		return UserEntity.builder().email(email).build();
	}

	// toDto 메서드
	public UserDto.LoginResponse toDtoLoginResponse() {
		return UserDto.LoginResponse.builder()
			.id(this.getId())
			.name(this.name)
			.email(this.email)
			.companyName(this.companyName)
			.roles(this.userRoles.stream()
				.map(userRole -> userRole.getRoleTypeCode().getCode())
				.collect(Collectors.toList()))
			.build();
	}

}
