package model;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import moteurJeu.*;
import utils.*;

public class SpaceInvaders implements Jeu  {

	int longueur;
	int hauteur;
	Vaisseau vaisseau;
	List<Missile> missile;
	//List missile = new LinkedList(); PASSER SUR DES COLLECTIONS 
	List<Envahisseur> envahisseur;
	boolean deplacementEnvahisseurVersLaDroite;
	boolean modificationDeplacementEnvahisseur;
	public boolean pretATirer = true;
	long tempsDifference = 0;
	
	public SpaceInvaders(int longueur, int hauteur) {
		this.longueur = longueur;
		this.hauteur = hauteur;
		deplacementEnvahisseurVersLaDroite = true;
		this.missile = new ArrayList<Missile>();
		this.envahisseur = new ArrayList<Envahisseur>();
	}

	public String recupererEspaceJeuDansChaineASCII() {
		StringBuilder espaceDeJeu = new StringBuilder();
		for (int y = 0; y < hauteur; y++) {
			for (int x = 0; x < longueur; x++) {
				espaceDeJeu.append(recupererMarqueDeLaPosition(x, y));
			}
			espaceDeJeu.append('\n');
		}
		return espaceDeJeu.toString();
	}

	private char recupererMarqueDeLaPosition(int x, int y) {
		char marque;
		if (this.VaisseauQuiOccupeLaPosition(y, x))
			marque = Constante.MARQUE_VAISSEAU;
		else if (this.MissileQuiOccupeLaPosition(y, x))
			marque = Constante.MARQUE_MISSILE;
		else if (this.EnvahisseurQuiOccupeLaPosition(y, x))
			marque = Constante.MARQUE_ENVAHISSEUR;
		else
			marque = Constante.MARQUE_VIDE;
		return marque;
	}

	private boolean VaisseauQuiOccupeLaPosition(int y, int x) {
		return this.aUnVaisseau() && vaisseau.occupeLaPosition(x, y);
	}

	public boolean aUnVaisseau() {
		return vaisseau != null;
	}

	private boolean EnvahisseurQuiOccupeLaPosition(int x, int y) {
		if (this.aUnEnvahisseur()) {
			for (int i=0; i < envahisseur.size(); i++) {
				if (envahisseur.get(i).occupeLaPosition(y, x))
					return true;
				}
			}
		return false;
	}

	public boolean aUnEnvahisseur() {
		return envahisseur != null;
	}
/*	public void positionnerNouvelEnvahisseur(Dimension dimension, Position position, int vitesse) {
		int x = position.abscisse();
		int y = position.ordonnee();

		if (!estDansEspaceJeu(x, y))
			throw new HorsEspaceJeuException("La position de l'envahisseur est en dehors de l'espace jeu");

		int longueurEnvahisseur = dimension.longueur();
		int hauteurEnvahisseur = dimension.hauteur();
		
		if (!estDansEspaceJeu(x + longueurEnvahisseur - 1, y))
			throw new DebordementEspaceJeuException("L'envahisseur déborde de l'espace jeu vers la droite à cause de sa longueur");
		if (!estDansEspaceJeu(x, y - hauteurEnvahisseur + 1))
			throw new DebordementEspaceJeuException("L'envahisseur déborde de l'espace jeu vers le bas à cause de sa hauteur");

		envahisseurs.add(new Envahisseur(dimension,position,vitesse));
	}*/
	private boolean estDansEspaceJeu(int x, int y) {
		return (((x >= 0) && (x < longueur)) && ((y >= 0) && (y < hauteur)));
	}

	public void deplacerVaisseauVersLaDroite() {
		if (vaisseau.abscisseLaPlusADroite() < (longueur - 1)) {
			vaisseau.deplacerHorizontalementVers(Direction.DROITE);
			if (!estDansEspaceJeu(vaisseau.abscisseLaPlusADroite(), vaisseau.ordonneeLaPlusHaute())) {
				vaisseau.positionner(longueur - vaisseau.longueur(), vaisseau.ordonneeLaPlusHaute());
			}
		}
	}

	public void deplacerVaisseauVersLaGauche() {
		if (0 < vaisseau.abscisseLaPlusAGauche())
			vaisseau.deplacerHorizontalementVers(Direction.GAUCHE);
		if (!estDansEspaceJeu(vaisseau.abscisseLaPlusAGauche(), vaisseau.ordonneeLaPlusHaute())) {
			vaisseau.positionner(0, vaisseau.ordonneeLaPlusHaute());
		}
	}
	public void positionnerUnNouveauVaisseau(Dimension dimension, Position position, int vitesse) {
		int x = position.abscisse();
		int y = position.ordonnee();

		if (!estDansEspaceJeu(x, y))
			throw new HorsEspaceJeuException("La position du vaisseau est en dehors de l'espace jeu");

		int longueurVaisseau = dimension.longueur();
		int hauteurVaisseau = dimension.hauteur();

		if (!estDansEspaceJeu(x + longueurVaisseau - 1, y))
			throw new DebordementEspaceJeuException(
					"Le vaisseau déborde de l'espace jeu vers la droite à cause de sa longueur");
		if (!estDansEspaceJeu(x, y - hauteurVaisseau + 1))
			throw new DebordementEspaceJeuException(
					"Le vaisseau déborde de l'espace jeu vers le bas à cause de sa hauteur");

		vaisseau = new Vaisseau(dimension, position, vitesse);
	}

	public void evoluer(Commande commandeUser) throws InterruptedException{
		System.out.println(pretATirer);
		if(pretATirer == false) {
			if(System.currentTimeMillis()>tempsDifference+320) {
				pretATirer = true;
			}
			else {
				pretATirer = false;
			}
		}
		if (commandeUser.gauche) {
			deplacerVaisseauVersLaGauche();
		}

		if (commandeUser.droite) {
			deplacerVaisseauVersLaDroite();
		}
		if (commandeUser.tir) {
			//On fait un système booleen + le temps en millisecondes comme ça on empèche le spam de missile
			//Thread.sleep(20); A mettre dans evoluer si ça ne fonctionne pas
			/*System.out.println("Temps actuel "+System.currentTimeMillis());
			System.out.println("Temps difference "+tempsDifference);
			System.out.println("Temps difference 220 "+tempsDifference +220);*/
			if(pretATirer) {
				tempsDifference = System.currentTimeMillis();
				tirerMissile(new Dimension(Constante.MISSILE_LONGUEUR, Constante.MISSILE_HAUTEUR),
						Constante.MISSILE_VITESSE);
				pretATirer = false;
			}

		}
		if (this.aUnMissile()) {
			this.deplacerMissile();
		}

		if (this.aUnEnvahisseur()) {
			this.deplacerEnvahisseur();
			this.eliminerEnvahisseur();
		}
	}
	public void eliminerEnvahisseur() {
		// LE Try-Catch résout le problème, mais ce n'est pas optimisé.
		try{
			for (int i=0; i < missile.size(); i++) {
				for (int j=0; j < envahisseur.size(); j++) {
					if (Collision.detecterCollision(envahisseur.get(j), missile.get(i))) {
						envahisseur.remove(j);
						missile.remove(i);
					}
				}
			}
		} catch(Exception e){
			System.out.println("Passe par l'exception");
		}
	}

	public boolean etreFini() {
		for(int i=0;i<envahisseur.size();i++) {
			if(recupererEnvahisseur().get(i) != null) {
				return false;
			}
		}
		return true;
	}

	/*public void initialiserJeu() {
		Position positionVaisseau = new Position(this.longueur / 2, this.hauteur - 1);
		Dimension dimensionVaisseau = new Dimension(Constante.VAISSEAU_LONGUEUR, Constante.VAISSEAU_HAUTEUR);
		positionnerUnNouveauVaisseau(dimensionVaisseau, positionVaisseau, Constante.VAISSEAU_VITESSE);

		Dimension dimensionEnvahisseur = new Dimension(Constante.ENVAHISSEUR_LONGUEUR, Constante.ENVAHISSEUR_HAUTEUR);
		Position positionEnvahisseur = new Position(this.longueur / 2, dimensionEnvahisseur.hauteur() - 1);
		positionnerUnNouvelEnvahisseur(dimensionEnvahisseur, positionEnvahisseur, Constante.ENVAHISSEUR_VITESSE);
	}*/
	
	public void initialiserJeu() {
		Position positionVaisseau = new Position(this.longueur / 2, this.hauteur - 1);
		Dimension dimensionVaisseau = new Dimension(Constante.VAISSEAU_LONGUEUR, Constante.VAISSEAU_HAUTEUR);
		positionnerUnNouveauVaisseau(dimensionVaisseau, positionVaisseau, Constante.VAISSEAU_VITESSE);

		ajouterGroupeEnvahisseurs();
	}

	public Vaisseau recupererVaisseau() {
		return this.vaisseau;
	}

	public List<Missile> recupererMissile() {
		return this.missile;
	}
	public Missile recupererUnUniqueMissile(int index) {
		return (Missile) this.missile.get(index);
	}
	
	public void tirerMissile(Dimension dimensionMissile, int vitesseMissile) {

		if ((vaisseau.hauteur() + dimensionMissile.hauteur()) > this.hauteur)
			throw new MissileException("Pas assez de hauteur libre entre le vaisseau et le haut de l'espace jeu pour tirer le missile");

		Missile nouveauMissile = this.vaisseau.tirerUnMissile(dimensionMissile,vitesseMissile);
		
		boolean isShootable = true; //S'il peut tirer
		
		for (int i=0; i < missile.size(); i++) {
			if (Collision.detecterCollision((Sprite) missile.get(i), nouveauMissile)) {
				isShootable = false;
			}
		}
		
		if (isShootable) {
			this.missile.add(nouveauMissile);
		}
		
 	}

	private boolean MissileQuiOccupeLaPosition(int x, int y) {
		if (this.aUnMissile()) {
			for (int i=0; i < missile.size(); i++) {
				if (((Sprite) missile.get(i)).occupeLaPosition(y, x)) {
					return true;
				}
			}
		}
		
		return false;
	}

	public boolean aUnMissile() {
		return !missile.isEmpty();
	}

	public void deplacerMissile() {
		if (this.aUnMissile()) {
			for (int i=0; i < missile.size(); i++) { 
				((Sprite) missile.get(i)).deplacerVerticalementVers(Direction.HAUT_ECRAN);
				if (!estDansEspaceJeu(((Sprite) missile.get(i)).abscisseLaPlusADroite(), ((Sprite) missile.get(i)).ordonneeLaPlusBasse())) {
					this.missile.remove(i);
				}
			}
		}
	}

	public void deplacerEnvahisseur() {

		if (this.envahisseurSeDeplaceVersLaDroite()) {
			this.deplacerEnvahisseurVersLaDroite();
		} else {
			this.deplacerEnvahisseurVersLaGauche();
		}

	}
	public void deplacerEnvahisseursVersLaGauche() {
		for (int i=0; i < envahisseur.size(); i++) {
			if (0 < envahisseur.get(i).abscisseLaPlusAGauche())
				envahisseur.get(i).deplacerHorizontalementVers(Direction.GAUCHE);
			if (!estDansEspaceJeu(envahisseur.get(i).abscisseLaPlusAGauche(), envahisseur.get(i).ordonneeLaPlusHaute())) {
				envahisseur.get(i).positionner(0, envahisseur.get(i).ordonneeLaPlusHaute());
			}
		}
	}
	public boolean envahisseurSeDeplaceVersLaDroite() {
		for (int i=0; i < envahisseur.size(); i++) {
			if (this.envahisseursEstAGauche(i)) {
				this.deplacementEnvahisseurVersLaDroite = true;
			} else if (this.envahisseursEstADroite(i)) {
				this.deplacementEnvahisseurVersLaDroite = false;
			}
		}

		return this.deplacementEnvahisseurVersLaDroite;
	}
	/*public boolean envahisseurSeDeplaceVersLaDroite() {
		if (this.envahisseurEstAGauche()) {
			this.deplacementEnvahisseurVersLaDroite = true;
			EnvahisseurDescend(Constante.ENVAHISSEUR_DESCEND);
		} else if (this.envahisseurEstADroite()) {
			this.deplacementEnvahisseurVersLaDroite = false;
			EnvahisseurDescend(Constante.ENVAHISSEUR_DESCEND);
			
			
		}

		return this.deplacementEnvahisseurVersLaDroite;
	}*/

	public void EnvahisseurDescend(int i) {
		for (int i1=0; i1 < envahisseur.size(); i1++) {
			envahisseur.get(i1).origine.y=envahisseur.get(i1).origine.y+i;
		}
	}
	/*public boolean envahisseurEstADroite() {
		return this.longueur - 1 == this.envahisseur.abscisseLaPlusADroite();
	}

	private boolean envahisseurEstAGauche() {
		return this.envahisseur.abscisseLaPlusAGauche() == 0;
	}*/

	public void positionnerNouvelEnvahisseur(Dimension dimension, Position position, int vitesse) {
		int x = position.abscisse();
		int y = position.ordonnee();

		if (!estDansEspaceJeu(x, y))
			throw new HorsEspaceJeuException("La position de l'envahisseur est en dehors de l'espace jeu");

		int longueurEnvahisseur = dimension.longueur();
		int hauteurEnvahisseur = dimension.hauteur();
		
		if (!estDansEspaceJeu(x + longueurEnvahisseur - 1, y))
			throw new DebordementEspaceJeuException("L'envahisseur déborde de l'espace jeu vers la droite à cause de sa longueur");
		if (!estDansEspaceJeu(x, y - hauteurEnvahisseur + 1))
			throw new DebordementEspaceJeuException("L'envahisseur déborde de l'espace jeu vers le bas à cause de sa hauteur");

		envahisseur.add(new Envahisseur(dimension,position,vitesse));
	}

	public void deplacerEnvahisseurVersLaDroite() {
		for (int i=0; i < envahisseur.size(); i++) {
			if (envahisseur.get(i).abscisseLaPlusADroite() < (longueur - 1)) {
				envahisseur.get(i).deplacerHorizontalementVers(Direction.DROITE);
				if (!estDansEspaceJeu(envahisseur.get(i).abscisseLaPlusADroite(), envahisseur.get(i).ordonneeLaPlusHaute())) {
					envahisseur.get(i).positionner(longueur - envahisseur.get(i).longueur(), envahisseur.get(i).ordonneeLaPlusHaute());
				}
			}
		}
	}
	
	public void deplacerEnvahisseurVersLaGauche() {
		for (int i=0; i < envahisseur.size(); i++) {
			if (0 < envahisseur.get(i).abscisseLaPlusAGauche())
				envahisseur.get(i).deplacerHorizontalementVers(Direction.GAUCHE);
			if (!estDansEspaceJeu(envahisseur.get(i).abscisseLaPlusAGauche(), envahisseur.get(i).ordonneeLaPlusHaute())) {
				envahisseur.get(i).positionner(0, envahisseur.get(i).ordonneeLaPlusHaute());
			}
		}
	}
	public void deplacerEnvahisseurs() {
		if (this.envahisseurSeDeplaceVersLaDroite()) {
			this.deplacerEnvahisseurVersLaDroite();
		} else {
				this.deplacerEnvahisseurVersLaGauche();
		}
	}
	public boolean envahisseurSeDeplaceVerLaDroite() {
		for (int i=0; i < envahisseur.size(); i++) {
			if (this.envahisseursEstAGauche(i)) {
				this.modificationDeplacementEnvahisseur = true;
			} else if (this.envahisseursEstADroite(i)) {
				this.modificationDeplacementEnvahisseur = false;
			}
		}

		return this.modificationDeplacementEnvahisseur;
	}

	public boolean envahisseursEstADroite(int index) {
		return this.longueur - 1 == this.envahisseur.get(index).abscisseLaPlusADroite();
	}

	public boolean envahisseursEstAGauche(int index) {
		return this.envahisseur.get(index).abscisseLaPlusAGauche() == 0;
	}
	
	public void ajouterGroupeEnvahisseurs(){
		int nbEnvahisseur = calculerNombreEnvahisseur();
		for(int i = 0 ; i < nbEnvahisseur ; i++) {
			positionnerNouvelEnvahisseur(new Dimension(Constante.ENVAHISSEUR_LONGUEUR, Constante.ENVAHISSEUR_HAUTEUR), new Position(calculerAbscisseEnvahisseur(i), Constante.ENVAHISSEUR_HAUTEUR*2),Constante.ENVAHISSEUR_VITESSE);
		}
	}
	public int calculerAbscisseEnvahisseur(int i) {
		return (i * (Constante.ENVAHISSEUR_LONGUEUR * 2));
	}

	public int calculerNombreEnvahisseur() {
		return (Constante.ESPACEJEU_LONGUEUR / (Constante.ENVAHISSEUR_LONGUEUR * 2));
	}
	public List<Envahisseur> recupererEnvahisseur() {
		return this.envahisseur;
	}

	/*public boolean collisiondeuxSprite() {
		return Collision.detecterCollision(this.missile, this.envahisseur);
	}*/
}
