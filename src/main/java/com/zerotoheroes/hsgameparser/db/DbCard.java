package com.zerotoheroes.hsgameparser.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
// http://stackoverflow.com/questions/4486787/jackson-with-json-unrecognized-field-not-marked-as-ignorable
@JsonIgnoreProperties(ignoreUnknown = true)
public class DbCard {

	private String id;
	private int dbfId;
	private int cost;
	private String name;
	private int attack;
	private int health;
	private String playerClass;
	private String type;
	private String rarity;
	private String set;
	private String text;
	private boolean collectible;
	private List<String> mechanics;

	public String getSafeName() {
		return name.replace("\"", "");
	}
}
