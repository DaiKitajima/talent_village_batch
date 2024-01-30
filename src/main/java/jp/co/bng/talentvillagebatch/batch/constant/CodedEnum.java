package jp.co.bng.talentvillagebatch.batch.constant;

/**
 * JDBCの変換処理にて「Enum<->Integer」する場合に当該クラスを継承すること。
 * ※Postgreではうまくいかないことを確認
 * 　以下のSQLを実行したとき、MySQLでは文字列リテラル「"1"」を数値「1」として認識してくれるが、
 * 　Postgreは型エラーとするため。
 * 　create table test(id int);
 * 　insert into test(id) values ("1");
 * @author shu.shun
 *
 */
public interface CodedEnum {
	
	public Integer getCode();

}
