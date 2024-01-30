package jp.co.bng.talentvillagebatch.db_chat.dao;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Daoの基底クラス
 * @author shu.shun
 *
 */
@Slf4j
public class DbBaseDao {

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/** 半角パーセント */
	private static final String PERCENT_CHAR = "%";

	/** 別パッケージから直接インスタンス生成をさせない */
	protected DbBaseDao() {

	}

	/**
	 * LIKE検索条件設定時に前方一致用の文字を付加して返却する
	 * @param cond
	 * @return
	 */
	protected String getLikeCondFrontward(String cond) {

		if (StringUtils.isEmpty(cond)) {
			return "";
		}

		return PERCENT_CHAR + cond;

	}

	/**
	 * LIKE検索条件設定時に後方一致用の文字を付加して返却する
	 * @param cond
	 * @return
	 */
	protected String getLikeCondBackward(String cond) {

		if (StringUtils.isEmpty(cond)) {
			return "";
		}

		return cond + PERCENT_CHAR;
	}

	/**
	 * LIKE検索条件設定時にあいまい検索用の文字を付加して返却する
	 * @param cond
	 * @return
	 */
	protected String getLikeCondBoth(String cond) {

		String preCond = this.getLikeCondFrontward(cond);
		String endCond = this.getLikeCondBackward(preCond);

		return endCond;
	}

	/**
	 * 最終登録ID取得
	 * @return
	 */
	public int getLastInsertId() {

		log.info("getLastInsertId START");

		String sql = "SELECT LAST_INSERT_ID()";

		Integer lastInsertId = null;
		try {
			lastInsertId = jdbcTemplate.queryForObject(sql.toString(), Integer.class);

		} catch (Exception e) {
			log.error("LastInsertID取得に失敗しました。", e);

		}

		log.info("getLastInsertId END");

		return lastInsertId;
	}

	/**
	 * 直前に発行されたSQLの総取得件数を取得する
	 * Limit句で取得件数に制限をかけている場合に使用する
	 * @return
	 */
	public int getTotalDataCountForLimit() {

		log.info("getTotalDataCountForLimit START");

		String sql = "SELECT FOUND_ROWS() as totalDataCount";

		int totalDataCount = 0;

		try {
			totalDataCount = jdbcTemplate.queryForObject(sql.toString(), Integer.class);
		} catch(Exception e) {
			log.error("TotalDataCountの取得に失敗しました。", e);
		}

		log.info("getTotalDataCountForLimit END");

		return totalDataCount;

	}
	
	/**
	 * 「target（カラムの名称）がcondListに含まれる値を1つでも含むか」の条件式を生成する。
	 * つまり、target集合とcondList集合に重なる部分があるかどうかの条件式を生成する。
	 * condListまたはtargetがNULLまたは空の場合、SQL上で必ずTRUEになるダミー条件式を返す。
	 * 制限：DB上でtargetがCSV形式文字列で格納されていること。
	 * @param condList
	 * @param target
	 * @return
	 */
	protected String generateConditionToGetIntersectionSets(List<Integer> condList, String target) {

		log.info("generateConditionToGetIntersectionSets START");

		// 引数チェック
		if (condList == null || condList.isEmpty() || StringUtils.isEmpty(target)) {
			return "(TRUE) ";
		}

		// メイン処理
		String conditionStr = condList.stream()
			.map(e -> "find_in_set('" + e + "'," + target + ") ")
			.collect(Collectors.joining("OR " , "(" , ")" ));


		log.info("generateConditionToGetIntersectionSets END");

		return conditionStr;
	}

	/**
	 * emptyチェックを目的とするメソッド。
	 * condがemptyの時、SQL上で必ずTRUEになるダミー条件式を返す。
	 * そうでない場合、condとoprとtargetを結合し、文字列として返す。
	 * emptyかどうかの判定は{@link ObjectUtils#isEmpty(Object)}に移譲する。
	 * ※SQLインジェクション対策していないため、自由入力項目には使用しないこと
	 * @param cond toString()したときに比較に有効な文字列になること
	 * @param opr SQLで有効な演算子を文字列リテラルで指定すること
	 * @param target 有効なカラム名を文字列リテラルで指定すること
	 * @return
	 */
	protected String trueIfCondEmptyOrElse(Object cond, String opr, String target) {
		if (ObjectUtils.isEmpty(cond)) {
			return "(TRUE) ";
		}else {
			return "(" + cond.toString() + " " + opr + " " + target + ") ";
		}
	}

	/**
	 * emptyチェックを目的とするメソッド。
	 * condがemptyの時、SQL上で必ずFALSEになるダミー条件式を返す。
	 * そうでない場合、condとoprとtargetを結合し、文字列として返す。
	 * emptyかどうかの判定は{@link ObjectUtils#isEmpty(Object)}に移譲する。
	 * ※SQLインジェクション対策していないため、自由入力項目には使用しないこと
	 * @param cond toString()したときに比較に有効な文字列になること
	 * @param opr SQLで有効な演算子を文字列リテラルで指定すること
	 * @param target 有効なカラム名を文字列リテラルで指定すること
	 * @return
	 */
	protected String falseIfCondEmptyOrElse(Object cond, String opr, String target){
		if (ObjectUtils.isEmpty(cond)) {
			return "(FALSE) ";
		}else {
			return "(" + cond.toString() + " " + opr + " " + target + ") ";
		}
	}

	/**
	 * emptyチェックを目的とするメソッド。
	 * condがemptyの時、SQL上で必ずTRUEになるダミー条件式を返す。
	 * そうでない場合、generatorにcondを適用して生成された文字列を返す。
	 * emptyかどうかの判定は{@link ObjectUtils#isEmpty(Object)}に移譲する。
	 * ※SQLインジェクション対策していないため、自由入力項目には使用しないこと
	 * @param <T> 
	 * @param cond
	 * @param generator
	 * @return
	 */
	protected <T> String trueIfCondEmptyOrElse(T cond, Function<T, String> generator) {
		if (ObjectUtils.isEmpty(cond)) {
			return "(TRUE) ";
		}else {
			return generator.apply(cond);
		}
	}

	/**
	 * emptyチェックを目的とするメソッド。
	 * condがemptyの時、SQL上で必ずFALSEになるダミー条件式を返す。
	 * そうでない場合、generatorにcondを適用して生成された文字列を返す。
	 * emptyかどうかの判定は{@link ObjectUtils#isEmpty(Object)}に移譲する。
	 * ※SQLインジェクション対策していないため、自由入力項目には使用しないこと
	 * @param <T> 
	 * @param cond
	 * @param generator
	 * @return
	 */
	protected <T> String falseIfCondEmptyOrElse(T cond, Function<T, String> generator){
		if (ObjectUtils.isEmpty(cond)) {
			return "(FALSE) ";
		}else {
			return generator.apply(cond);
		}
	}

}
