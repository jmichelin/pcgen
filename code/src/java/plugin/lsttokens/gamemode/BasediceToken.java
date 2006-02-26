package plugin.lsttokens.gamemode;

import pcgen.core.GameMode;
import pcgen.persistence.lst.BaseDiceLoader;
import pcgen.persistence.lst.GameModeLstToken;

/**
 * Class deals with BASEDICE Token
 */
public class BasediceToken implements GameModeLstToken {

	public String getTokenName() {
		return "BASEDICE";
	}

	//FLP WEAPONSIZEPENALTY3.5
	public boolean parse(GameMode gameMode, String value) {
		try {
			BaseDiceLoader baseDiceLoader = new BaseDiceLoader();
			baseDiceLoader.parseLine(gameMode, "BASEDICE:" + value);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
}
