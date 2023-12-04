package mg.fita.kinkinasaifu;

import mg.fita.kinkinasaifu.connection.ConnectionDB;

public class KinkinaSaifuApplication {

	public static void main(String[] args) {
		ConnectionDB.getConnection();
	}

}
