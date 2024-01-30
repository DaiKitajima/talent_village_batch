package jp.co.bng.talentvillagebatch.batch.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.springframework.util.ObjectUtils;

public class DateTimeConverter {
	public static LocalDateTime toSystemDefault(LocalDateTime utc) {
		return ObjectUtils.isEmpty(utc)
				? null
				: utc
				.atZone(ZoneId.of("UTC"))
				.withZoneSameInstant(ZoneOffset.systemDefault())
				.toLocalDateTime();
	}
	public static LocalDateTime toUtc(LocalDateTime local) {
		return ObjectUtils.isEmpty(local)
				? null
				: local
				.atZone(ZoneId.systemDefault())
				.withZoneSameInstant(ZoneOffset.UTC)
				.toLocalDateTime();
	}
}
