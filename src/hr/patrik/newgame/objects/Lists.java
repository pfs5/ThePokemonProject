package hr.patrik.newgame.objects;

import java.util.ArrayList;

public class Lists {

	private ArrayList<Pokemon> pokemonList;
	private ArrayList<Attack> attackList;
	private ArrayList<Item> itemList;
	
	public Lists () {
		initLists();
	}
	
	private void initLists() {
		
	}
	
	//Getters
	public ArrayList<Pokemon> getPokemonList() {
		return pokemonList;
	}
	public ArrayList<Attack> getAttackList() {
		return attackList;
	}
	public ArrayList<Item> getItemList() {
		return itemList;
	}
}
