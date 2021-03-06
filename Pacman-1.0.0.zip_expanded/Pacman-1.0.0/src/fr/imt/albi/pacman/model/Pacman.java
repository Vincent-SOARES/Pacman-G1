package fr.imt.albi.pacman.model;

import fr.imt.albi.pacman.main.PacManLauncher;
import fr.imt.albi.pacman.utils.ArcCircle;
import fr.imt.albi.pacman.utils.Figure;
import fr.imt.albi.pacman.utils.Food;
import fr.imt.albi.pacman.utils.Wall;

public class Pacman extends Creature {
	/* L'angle d'ouverture mini de sa bouche quand il avance */
	public static final int MIN_MOUTH_ANGLE = 10;
	/* L'angle d'ouverture mini de sa bouche quand il avance */
	public static final int MAX_MOUTH_ANGLE = 40;
	/* Sa vitesse sur la grille */
	public static final int SPEED_PACMAN = 10;
	/* La couleur de Pacman */
	private static final String PACMAN_COLOR = "yellow";
	/* Le nombre initial de vies de Pacman */
	private static final int LIFE_START = 3;
	/* Le nombre de points pour qu'il obtienne une vie */
	private static final int LIFE_POINT_THRESHOLD = 10000;

	private final ArcCircle pacman;
	private int mouthAngle;
	private boolean isMouthOpen;
	private boolean isEmpowered;
	private String lastPosition;
	private String lastMovement;
	private int currentLife;
	private int currentScore;
	private int nextLifeThreshold;
	private int nbViesBonus;
	public Pacman(int size, int x, int y) {
		this.pacman = new ArcCircle(size, x, y, PACMAN_COLOR, 0, 360);

		this.lastPosition = PacManLauncher.LEFT;
		this.mouthAngle = MIN_MOUTH_ANGLE;
		this.handleMouthOpening(PacManLauncher.LEFT);
		this.currentLife = LIFE_START;
		this.isEmpowered = false;
		this.lastMovement = PacManLauncher.LEFT;
		this.nextLifeThreshold = Pacman.LIFE_POINT_THRESHOLD;
		this.nbViesBonus=0;
	}

	public void removeLife() {
		this.currentLife--;
		// TODO Méthode qui gère le retrait d'une vie à Pacman
	}

	public int getCurrentLife() {
		return this.currentLife;
	}

	public void updateScoreFood(boolean PowerUp) {
		if (PowerUp==true){
			this.currentScore+=50;
		}
		else {
			this.currentScore+=10;
		}	
		checkIfNewLife();
	}
	// TODO Là, si Pacman a reçu un power-up, faut incrémenter le score comme il se
			// doit.
	private void checkIfNewLife() {
		if (this.currentScore-(this.nbViesBonus)*this.nextLifeThreshold>=this.nextLifeThreshold) {
			nbViesBonus++;
			this.currentLife+=1;
		}
	}
	// TODO Là, faut vérifier si le Pacman a atteint la limite pour avoir une vie
			// supplémentaire :)
	public void updateScoreGhost() {
			this.currentScore+=200;
			checkIfNewLife();
			}
		// TODO Là, si Pacman bouffe un fantome, faut incrémenter le score comme il faut
		// aussi.
	

	public int getCurrentScore() {
		return this.currentScore;
	}

	@Override
	public int getSpeed() {
		return Pacman.SPEED_PACMAN;
	}

	@Override
	public int getX() {
		return this.pacman.getX();
	}

	@Override
	public int getY() {
		return this.pacman.getY();
	}

	@Override
	public int getWidth() {
		return this.pacman.getWidth();
	}

	@Override
	public void draw() {
		this.pacman.draw();
	}

	@Override
	public void move(String direction) {
		int xMove = 0;
		int yMove = 0;

		if (this.isMovePossible(direction)) {
			xMove = navigateInMap(direction)[0];
            yMove = navigateInMap(direction)[1];
            int[] coords = this.checkCollision(direction, xMove, yMove);
            
            handleMouthOpening(direction);
            animateMouth();
            //interactWithFood(this.gameMap.getMap(), (this.getY() + yMove) / gameMap.getSizeCase(), (this.getX() + xMove)/ gameMap.getSizeCase());

            this.move(coords[0],coords[1]);
            if (this.getY()+yMove>gameMap.getSizeCase()*gameMap.getNbCases()) {
            	this.setLocation(this.getX()+xMove, 0);
            }
            if (this.getX()+xMove>gameMap.getSizeCase()*gameMap.getNbCases()) {
            	this.setLocation(0, this.getY()+yMove);
            }
            if (this.getY()+yMove<0) {
            	this.setLocation(this.getX()+xMove, gameMap.getSizeCase()*gameMap.getNbCases());
            }
            if (this.getX()+xMove<0) {
            	this.setLocation(gameMap.getSizeCase()*gameMap.getNbCases(), this.getY()+yMove);
            }
            this.lastMovement = direction;

			/*
			 * TODO Si le déplacement est possible, il faut : - récupérer les nouvelles
			 * coordonnées, - voir avec quoi on risque de se percuter avec ces nouvelles
			 * coordonnées et agir en conséquence (i.e. remettre à jour les coords) - se
			 * déplacer - garder une trace du dernier déplacement effectué (y a un attribut
			 * de classe pour ça) - Animer sa bouche ;)
			 */
		} else{
			xMove = navigateInMap(this.lastMovement)[0];
	        yMove = navigateInMap(this.lastMovement)[1];
	        int[] coords = this.checkCollision(this.lastMovement, xMove, yMove);
			handleMouthOpening(this.lastMovement);
            animateMouth();
            //interactWithFood(this.gameMap.getMap(), (this.getY() + yMove) / gameMap.getSizeCase(), (this.getX() + xMove)/ gameMap.getSizeCase());
            this.move(coords[0],coords[1]);
            if (this.getY()+yMove>gameMap.getSizeCase()*gameMap.getNbCases()) {
            	this.setLocation(this.getX()+xMove, 0);
            }
            if (this.getX()+xMove>gameMap.getSizeCase()*gameMap.getNbCases()) {
            	this.setLocation(0, this.getY()+yMove);
            }
            if (this.getY()+yMove<0) {
            	this.setLocation(this.getX()+xMove, gameMap.getSizeCase()*gameMap.getNbCases());
            }
            if (this.getX()+xMove<0) {
            	this.setLocation(gameMap.getSizeCase()*gameMap.getNbCases(), this.getY()+yMove);
            }
		}
			/*
			 * TODO Si le déplacement n'est possible, il faut pouvoir récupérer les
			 * coordonnées en partant du principe que sa direction sera égale à la dernière
			 * direction qui avait marché. Quasiment la même chose, juste que ça sera pas
			 * direction qui sera utilisé, mais autre chose :) Faut toujours animer sa
			 * bouche ceci dit !
			 */
		
	}

	/**
	 * Cette méthode permet de vérifier si le déplacement demandé est effectivement
	 * faisable.
	 *
	 * @param direction La direction choisie
	 * @return true si possible, false sinon
	 */
	private boolean isMovePossible(String direction) {
        boolean canMove = false;
        Figure[][] map = this.gameMap.getMap();

        if (this.getX() % this.gameMap.getSizeCase() == 0 && this.getY() % this.gameMap.getSizeCase() == 0) {
            int[] position = this.getColumnAndRow();
            int xPosition = position[0];
            int yPosition = position[1];

            Figure fUp = map[yPosition - 1][xPosition];
            Figure fDown = map[yPosition + 1][xPosition];
            Figure fleft = map[yPosition][xPosition - 1];
            Figure fRight = map[yPosition][xPosition + 1];

            switch (direction) {
                case PacManLauncher.UP:
                    if (!(fUp instanceof Wall)) {
                        canMove = true;
                    }
                    break;
                case PacManLauncher.DOWN:
                    if (!(fDown instanceof Wall)) {
                        canMove = true;
                    }
                    break;
                case PacManLauncher.LEFT:
                    if (!(fleft instanceof Wall)) {
                        canMove = true;
                    }
                    break;
                case PacManLauncher.RIGHT:
                    if (!(fRight instanceof Wall)) {
                        canMove = true;
                    }
                    break;
            }
        }

        return canMove;
    }

	@Override
	public void move(int xMove, int yMove) {
		this.pacman.move(xMove, yMove);
	}

	/**
	 * Anime la bouche du petit aussi, mais avec les calculs qui vont bien
	 *
	 * @param direction La direction à laquelle pointe Pacman
	 */
	private void handleMouthOpening(String direction) {
		int startAngle = 0;
		int extentAngle = 0;

		if (direction.equals(PacManLauncher.UP)) {
			startAngle = 90 - this.mouthAngle;
			extentAngle = -360 + 2 * this.mouthAngle;
		} else if (direction.equals(PacManLauncher.LEFT)) {
			startAngle = 180 - this.mouthAngle;
			extentAngle = -360 + 2 * this.mouthAngle;
		} else if (direction.equals(PacManLauncher.DOWN)) {
			startAngle = 270 - this.mouthAngle;
			extentAngle = -360 + 2 * this.mouthAngle;
		} else if (direction.equals(PacManLauncher.RIGHT)) {
			startAngle = -this.mouthAngle;
			extentAngle = -360 + 2 * this.mouthAngle;
		}

		this.pacman.setAngleStart(startAngle);
		this.pacman.setAngleExtent(extentAngle);
		this.lastPosition = direction;
	}

	@Override
	protected void interactWithFood(Figure[][] map, int i, int j) {
		Figure f = map[i][j];
		if (f instanceof Food) {
			Food food = (Food) f;
			if (food.getFood() != null) {
				this.isEmpowered=food.isPowerUp();
				updateScoreFood(food.isPowerUp());
				food.setFood(null);
				food.draw();
				map[i][j]=food;
				gameMap.pickFood();							
				/*
				 * TODO Ici, il faut: - Changer le food en null (y a un setFood...) - Redessiner
				 * le food (.draw()) - Et après, remettre à jour la map en updatant la bouffe
				 * qu'il y avait dedans - Mettre à jour le score - Sachant qu'un food peut être
				 * un powerup, y a un truc à gérer :)
				 */
			}
		}
	}

	public boolean getIsEmpowered() {
		return this.isEmpowered;
	}

	public void resetIsEmpowered() {
		this.isEmpowered = false;
	}

	@Override
	public boolean checkCaseType(Figure f) {
		return f instanceof Wall || f instanceof Food;
	}

	/**
	 * Anime la bouche du petit.
	 */
	public void animateMouth() {
		if (this.isMouthOpen) {
			this.mouthAngle = MIN_MOUTH_ANGLE;
		} else {
			this.mouthAngle = MAX_MOUTH_ANGLE;
		}
		this.handleMouthOpening(this.lastPosition);
		this.isMouthOpen = !this.isMouthOpen;
	}

	/**
	 * Méthode qui permet de dire s'il se pète la gueule avec un fantome.
	 *
	 * @param f Le fantome en question
	 * @return true ou false
	 */
	public boolean isPacmanCollidingWithGhost(Ghost f) {
		int xGhost = f.getX();
		int yGhost = f.getY();
		int sizeGhost = f.getWidth();

		int xPacman = this.getX();
		int yPacman = this.getY();
		int sizePacman = this.getWidth();

		boolean posMinX = xPacman < xGhost + sizeGhost || xPacman + sizePacman < xGhost + sizeGhost;
		boolean posMaxX = xPacman > xGhost || xPacman + sizePacman > xGhost;
		boolean posMinY = yPacman < yGhost + sizeGhost || yPacman + sizePacman < yGhost + sizeGhost;
		boolean posMaxY = yPacman > yGhost || yPacman + sizePacman > yGhost;

		return posMinX && posMaxX && posMinY && posMaxY;
	}
}
