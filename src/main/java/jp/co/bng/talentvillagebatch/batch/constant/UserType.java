package jp.co.bng.talentvillagebatch.batch.constant;

import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author shu.shun
 *
 */
@RequiredArgsConstructor
public enum UserType implements CodedEnum {
	CANDIDATE(1, "候補者"),
	CONSULTANT(2, "コンサルタント");

	private final Integer code;// 画面から選択肢として受け取る用
	@Getter
	private final String label;// 画面表示用
	
	@Override
	public Integer getCode() {
		return code;
	}
	
	public static UserType of(Integer code) {
		return Stream.of(UserType.values())
				.filter(wtc -> wtc.getCode().equals(code))
				.findAny()
				.orElse(null);
	}

}
