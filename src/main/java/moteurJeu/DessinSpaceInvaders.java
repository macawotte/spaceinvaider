package moteurJeu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import model.*;
import java.util.*;
public class DessinSpaceInvaders implements DessinJeu {

	private SpaceInvaders jeu;

	   public DessinSpaceInvaders(SpaceInvaders spaceInvaders) {
		   this.jeu = spaceInvaders;
	   }
	   public void dessiner(BufferedImage im) {
			if (this.jeu.aUnVaisseau()) {
				Vaisseau vaisseau = this.jeu.recupererVaisseau();
				this.dessinerVaisseau(vaisseau, im);
			}
			if (this.jeu.aUnMissile()) {
				List<Missile> missiles = this.jeu.recupererMissile();
				for (int i=0; i < missiles.size(); i++) {
					if (missiles.get(i) != null) {
						this.dessinerMissile(missiles.get(i), im);
					}
				}
			}
			if (this.jeu.aUnEnvahisseur()) {
				List<Envahisseur> envahisseurs = this.jeu.recupererEnvahisseur();
				for (int j=0; j < envahisseurs.size(); j++) {
					if (envahisseurs.get(j) != null) {
						this.dessinerEnvahisseur(envahisseurs.get(j), im);
					}
				}
			}
		}

	   private void dessinerVaisseau(Vaisseau vaisseau, BufferedImage im) {
		   Graphics2D crayon = (Graphics2D) im.getGraphics();
		    crayon.setColor(Color.red);
		   	crayon.fillRect(vaisseau.abscisseLaPlusAGauche(), vaisseau.ordonneeLaPlusBasse(), vaisseau.longueur(), vaisseau.hauteur());

	   }
	   
	   
	   private void dessinerEnvahisseur(Envahisseur envahisseur, BufferedImage im) {
		   Graphics2D crayon = (Graphics2D) im.getGraphics();
		   crayon.setColor(Color.red);
		   crayon.fillRect(envahisseur.abscisseLaPlusAGauche(), envahisseur.ordonneeLaPlusBasse(),envahisseur.longueur(),envahisseur.hauteur());

	   }
	   
	   private void dessinerMissile(Missile missile, BufferedImage im) {
		   Graphics2D crayon = (Graphics2D) im.getGraphics();
		   crayon.setColor(Color.blue);
		   crayon.fillRect(missile.abscisseLaPlusAGauche(), missile.ordonneeLaPlusBasse(),missile.longueur(),missile.hauteur());

	   }


}
