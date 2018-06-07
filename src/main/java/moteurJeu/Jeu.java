package moteurJeu;

/**
 * represente un jeu un jeu est caracterise par la methode evoluer a redefinir
 * 
 * @author Graou
 *
 */
public interface Jeu {

	/**
	 * methode qui contient l'evolution du jeu en fonction de la commande
	 * 
	 * @param commandeUser
	 *            commande utilisateur
	 * @throws InterruptedException 
	 */
	public void evoluer(Commande commandeUser) throws InterruptedException;
	
	/**
	 * @return true si et seulement si le jeu est fini
	 */
	public boolean etreFini();
}
