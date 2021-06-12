package fr.imt.albi.pacman.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import fr.imt.albi.pacman.utils.Canvas;
import fr.imt.albi.pacman.utils.Figure;
import fr.imt.albi.pacman.utils.Food;
import fr.imt.albi.pacman.utils.MapGenerate;
import fr.imt.albi.pacman.utils.Wall;

public class GameMap {

	/**
	 * Taille de la fenêtre
	 */
	private final int WIDTH = Canvas.WIDTH;
	/**
	 * Le nombre de case de la map
	 */
	private int nbCases;
	/**
	 * La taille de chacune des cases
	 */
	private int sizeCase;
	/**
	 * La position en X de pacman
	 */
	private int pacmanX;
	/**
	 * La position en Y de pacman
	 */
	private int pacmanY;
	/**
	 * Tableau à deux dimension de figure contenant toute les figures de la map case
	 * par case
	 */
	private MapGenerate gameMap;
	/**
	 * La couleur des mur de la map
	 */
	private String wallColor;
	private String wallColord;
	private String wallColort;
	private String wallColorq;
	private String wallColorc;
	private String wallColors;
	private String wallColorse;
	private String wallColorh;
	/**
	 * Le nom du fichier .map
	 */
	private final String mapFile;
	/**
	 * Le nombre de gomme présent sur la map
	 */
	private int nbGom;
	/**
	 * La position sur la map de chaque fantôme en début de niveau : Un liste de
	 * couple (x,y)
	 */
	private final ArrayList<Integer[]> ghosts;

	public GameMap(int mapNumber) {
		this.mapFile = "./resources/map" + mapNumber + ".map";
		this.nbGom = 0;
		this.ghosts = new ArrayList<>();
		this.createMap();
	}

	/*******************************************************************
	 * Un fichier map.txt définira un niveau de jeu La première ligne contient les
	 * parametre nbCase, couleur du mur ... # = un mur . = une gomme = une
	 * super-gomme O = un chemin vide P = PacMan F = fantome
	 *******************************************************************/

	private void createMap() {
		try {

			InputStream ips = new FileInputStream(this.mapFile);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);

			boolean firstLine = true;
			String ligne;
			int i = 0;

			while ((ligne = br.readLine()) != null) {
				if (firstLine) {
					firstLine = false;
					String[] param = ligne.split(";");
					this.nbCases = Integer.parseInt(param[0]);
					this.sizeCase = this.WIDTH / this.nbCases;
					this.wallColor = param[1];
					this.wallColord = param[2];
					this.wallColort = param[3];
					this.wallColorq = param[4];
					this.wallColorc = param[5];
					this.wallColors = param[6];
					this.wallColorse = param[7];
					this.wallColorh = param[8];
					this.gameMap = new MapGenerate(this.nbCases);
				} else {
					int j = 0;
					int tmpx = 0;
					int tmpy = 0;
					Integer[] posGhost = null;

					String[] param = ligne.split("");
					for (String str : param) {
						tmpx = j * this.sizeCase;
						tmpy = i * this.sizeCase;

						switch (str) {
							case "#":
								this.gameMap.setFigure(i, j, new Wall(this.sizeCase, tmpx, tmpy, this.wallColor));
								break;
							case "�":
								this.gameMap.setFigure(i, j, new Wall(this.sizeCase, tmpx, tmpy, this.wallColord));
								break;
							case "�":
								this.gameMap.setFigure(i, j, new Wall(this.sizeCase, tmpx, tmpy, this.wallColort));
								break;
							case "�":
								this.gameMap.setFigure(i, j, new Wall(this.sizeCase, tmpx, tmpy, this.wallColorq));
								break;
							case "�":
								this.gameMap.setFigure(i, j, new Wall(this.sizeCase, tmpx, tmpy, this.wallColorc));
								break;
							case "&":
								this.gameMap.setFigure(i, j, new Wall(this.sizeCase, tmpx, tmpy, this.wallColors));
								break;
							case "Q":
								this.gameMap.setFigure(i, j, new Wall(this.sizeCase, tmpx, tmpy, this.wallColorse));
								break;
							case "C":
								this.gameMap.setFigure(i, j, new Wall(this.sizeCase, tmpx, tmpy, this.wallColorh));
								break;
							case ".":
								this.gameMap.setFigure(i, j, new Food(this.sizeCase, tmpx, tmpy, false));
								this.nbGom += 1;
								break;
							case "*":
								this.gameMap.setFigure(i, j, new Food(this.sizeCase, tmpx, tmpy, true));
								this.nbGom += 1;
								break;
							case "O":
								this.gameMap.setFigure(i, j, new Food(this.sizeCase, tmpx, tmpy));
								break;
							case "P":
								this.gameMap.setFigure(i, j, new Food(this.sizeCase, tmpx, tmpy));
								this.pacmanX = tmpx;
								this.pacmanY = tmpy;
								break;
							case "F":
								this.gameMap.setFigure(i, j, new Food(this.sizeCase, tmpx, tmpy));
								posGhost = new Integer[2];
								posGhost[0] = new Integer(tmpx);
								posGhost[1] = new Integer(tmpy);
								this.ghosts.add(posGhost);
								break;
						}
						j++;
					}
					i++;
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		assert this.nbGom > 0 : "Nombre de gommes nul";
		assert this.pacmanX > 0 : "Pacman non initialisé";
		assert this.pacmanY > 0 : "Pacman non initialisé";
		assert this.wallColor == "blue" || this.wallColor == "green" || this.wallColor == "pink"
				: "Mauvaise couleur de mur";
	}

	public void pickFood() {
		this.nbGom -= 1;
	}

	public int getNbGom() {
		return this.nbGom;
	}

	public Figure[][] getMap() {
		return this.gameMap.getGameMap();
	}

	public void draw() {
		this.gameMap.draw();
	}

	public int getNbCases() {
		return this.nbCases;
	}

	public int getSizeCase() {
		return this.sizeCase;
	}

	public int getPacmanX() {
		return this.pacmanX;
	}

	public int getPacmanY() {
		return this.pacmanY;
	}

	public ArrayList<Integer[]> getGhosts() {
		return this.ghosts;
	}
}