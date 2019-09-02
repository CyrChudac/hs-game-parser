package com.zerotoheroes.hsgameparser.achievements.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotoheroes.hsgameparser.achievements.GameEvents;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import com.zerotoheroes.hsgameparser.achievements.Requirement;
import com.zerotoheroes.hsgameparser.db.CardsList;
import com.zerotoheroes.hsgameparser.db.DbCard;
import lombok.SneakyThrows;
import org.assertj.core.api.WithAssertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.Requirement.CARD_PLAYED_OR_CHANGED_ON_BOARD;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.CORRECT_OPPONENT;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DUNGEON_RUN_STEP;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_WON;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.PASSIVE_BUFF;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.PLAYER_HERO;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.SCENARIO_IDS;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.SCENE_CHANGED_TO_GAME;
import static com.zerotoheroes.hsgameparser.achievements.ScenarioIds.DUNGEON_RUN;
import static org.assertj.core.util.Lists.newArrayList;

public class DungeonRunAchievements implements WithAssertions {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void generate_achievements() throws Exception {
        List<RawAchievement> progression = buildProgressionAchievements();
        List<RawAchievement> boss = buildBossAchievements();
        List<RawAchievement> treasures = buildTreasureAchievements();
        List<RawAchievement> passives = buildPassiveAchievements();
        List<RawAchievement> result = Stream.of(progression, boss, treasures, passives)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        List<String> serializedAchievements = result.stream()
                .map(this::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> buildProgressionAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> heroes = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("HERO_\\d{2}"))
                .collect(Collectors.toList());
        List<RawAchievement> result = heroes.stream()
                .flatMap(card -> {
                    List<RawAchievement> allSteps = new ArrayList<>();
                    for (int i = 0; i < 8; i++) {
                        allSteps.add(buildProgressionStep(card, i));
                    }
                    return allSteps.stream();
                })
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(8 * 9);
        return result;
    }

    private RawAchievement buildProgressionStep(DbCard card, int i) {
        String name = card.getName() + " (" + card.getPlayerClass() + ") - Cleared round " + (i + 1);
        return RawAchievement.builder()
                .id("dungeon_run_progression_" + card.getId() + "_" + i)
                .type("dungeon_run_progression")
                .name(name)
                .displayName(name)
                .emptyText("Clear the first round with " + card.getPlayerClass() + " to get started")
                .text("Cleared round " + (i + 1))
                .displayCardId(card.getId())
                .displayCardType(card.getType().toLowerCase())
                .difficulty(i == 7 ? "epic" : "free")
                .points(1 + 2 * i)
                .requirements(newArrayList(
                        Requirement.builder().type(DUNGEON_RUN_STEP).values(newArrayList("" + i)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(PLAYER_HERO).values(newArrayList(card.getId())).build(),
                        Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + DUNGEON_RUN)).build()
                ))
                .resetEvents(Lists.newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> buildBossAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> bossCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().startsWith("LOOTA_BOSS"))
                .filter(card -> !Arrays.asList(
                        "LOOTA_BOSS_53h2", // Inara the Mage
                        "LOOTA_BOSS_32h" // Karl and George
                        )
                        .contains(card.getId()))
                .filter(card -> "Hero".equals(card.getType()))
                .collect(Collectors.toList());
        List<RawAchievement> encounters = bossCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("dungeon_run_boss_encounter_" + card.getId())
                        .type("dungeon_run_boss_encounter")
                        .name(card.getName())
                        .displayName("Boss met: " + card.getName())
                        .emptyText(null)
                        .text(card.getText())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .difficulty("rare")
                        .points(2)
                        .requirements(newArrayList(
                                Requirement.builder().type(CORRECT_OPPONENT).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(SCENE_CHANGED_TO_GAME).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + DUNGEON_RUN)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_END))
                        .build())
                .collect(Collectors.toList());
        List<RawAchievement> victories = bossCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("dungeon_run_boss_victory_" + card.getId())
                        .type("dungeon_run_boss_victory")
                        .name(card.getName())
                        .displayName("Boss defeated: " + card.getName())
                        .emptyText(null)
                        .text(card.getText())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .difficulty("rare")
                        .points(3)
                        .requirements(newArrayList(
                                Requirement.builder().type(CORRECT_OPPONENT).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(GAME_WON).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + DUNGEON_RUN)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START))
                        .build())
                .collect(Collectors.toList());
        List<RawAchievement> result = Stream.concat(encounters.stream(), victories.stream())
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(48 * 2);
        return result;
    }

    private List<RawAchievement> buildTreasureAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> treasureCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("LOOTA_\\d{3}[a-z]?")
                        || card.getId().equals("LOOT_998l") // Wondrous wand
                        || card.getId().equals("LOOT_998k")) // Golden kobold
                .filter(card -> !card.getId().equals("LOOTA_842t")) // Forging of Quel'Delar
                .filter(card -> !Arrays.asList("LOOTA_102", "LOOTA_103", "LOOTA_104", "LOOTA_105", "LOOTA_107", "LOOTA_109")
                        .contains(card.getId())) // Minions summoned by bosses
                .filter(card -> card.getMechanics() == null || !card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> "Minion".equals(card.getType())
                        || "Spell".equals(card.getType())
                        || "Weapon".equals(card.getType()))
                .collect(Collectors.toList());
        List<RawAchievement> result = treasureCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("dungeon_run_treasure_play_" + card.getId())
                        .type("dungeon_run_treasure_play")
                        .name(card.getName())
                        .displayName("Treasure played: " + card.getName())
                        .emptyText(null)
                        .text(card.getText())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .difficulty("rare")
                        .points(3)
                        .requirements(newArrayList(
                                Requirement.builder().type(CARD_PLAYED_OR_CHANGED_ON_BOARD).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + DUNGEON_RUN)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(32);
        return result;
    }

    private List<RawAchievement> buildPassiveAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> pasiveCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("LOOTA_\\d{3}[a-z]?"))
                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .collect(Collectors.toList());
        List<RawAchievement> result = pasiveCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("dungeon_run_passive_play_" + card.getId())
                        .type("dungeon_run_passive_play")
                        .name(card.getName())
                        .displayName("Passive ability triggered: " + card.getName())
                        .emptyText(null)
                        .text(card.getText().replace("<b>Passive</b>", ""))
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .difficulty("rare")
                        .points(3)
                        .requirements(newArrayList(
                                Requirement.builder().type(PASSIVE_BUFF).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + DUNGEON_RUN)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(14);
        return result;
    }

    @SneakyThrows
    private String serialize(RawAchievement rawAchievement) {
        return mapper.writeValueAsString(rawAchievement);
    }
}