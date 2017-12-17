package tableur;

import java.util.Scanner;

public class Demo {

	public static void main(String[] ars) {
		String filename = null;
		Tableur t = new Tableur();
		boolean quit = true;
		Scanner scan = new Scanner(System.in);
		String entry;
		String tampon;
		Boolean bool;
		do {
			System.out.println("Que voulez-vous faire?");
			System.out.println("1 -> Ouvrir un fichier");
			System.out.println("2 -> Commencer un nouveau fichier");
			System.out.println("3 -> Enregistrer le fichier");
			System.out.println("4 -> Effacer une cellule");
			System.out.println("5 -> Modifier une cellule");
			System.out.println("6 -> Propager une cellule");
			System.out.println("7 -> Afficher le détail d'une cellule");
			System.out.println("8 -> Afficher l'ensemble des cellules");
			System.out.println("9 -> Ajouter une \"ligne\"");
			System.out.println("0 -> Ajouter une \"colonne\"");
			System.out.println("q -> Quitter");
			entry = scan.next();
			switch(entry) {
			case "1":
				System.out.println("Entrez le nom du fichier");
				entry = scan.next();
				if(t.ouvrir(entry)) {
					filename = entry;
					System.out.println("Ouverture réussie");
				}else {
					System.out.println("Ouverture échouée");
				}
				break;
			case "2":
				filename="";
				t = new Tableur();
				break;
			case "3":
				System.out.print("Entrez le nom du fichier");
				if(filename!=null) {
					System.out.println(" ("+filename+" ?)");
				}else {
					System.out.println("");
				}
				entry = scan.next();
				filename = entry;
				t.enregistrer(entry);
				break;
			case "4":
				System.out.println("Entrez le nom de la cellule à effacer");
				entry = scan.next();
				t.supprimerDonnee(entry);
				break;
			case "5":
				System.out.println("Entrez le nom de la cellule à modifier");
				tampon = scan.next();
				System.out.println("Entrez le contenu à lui donner");
				entry = scan.next();
				if((bool = t.modifierDonnee(tampon, entry))!=null && bool==false) {
					System.out.println("Tentative de création de cycle... Modification annulée");
				}
				break;
			case "6":
				System.out.println("Entrez le nom de la cellule à propager");
				tampon = scan.next();
				System.out.println("Entrez le nom de la cellule cible");
				entry = scan.next();
				if((bool = t.propagerDonnee(tampon, entry))!=null && bool==false) {
					System.out.println("Tentative de création de cycle... Modification annulée");
				}
				break;
			case "7":
				System.out.println("Entrez le nom de la cellule");
				entry = scan.next();
				if(!(tampon=t.getInfosCellule(entry)).equals("")) {
					System.out.print(tampon);
				}
				break;
			case "8":
				System.out.print(t.cellsToString());
				break;
			case "9":
				t.addLigne();
				break;
			case "0":
				t.addColonne();
				break;
			case "q":
				quit = false;
				break;
			}
			
		}while(quit);
		scan.close();
	}	
}
