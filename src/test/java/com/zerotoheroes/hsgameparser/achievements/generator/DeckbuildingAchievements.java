package com.zerotoheroes.hsgameparser.achievements.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotoheroes.hsgameparser.achievements.GameEvents;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import com.zerotoheroes.hsgameparser.achievements.Requirement;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.FormatType.STANDARD;
import static com.zerotoheroes.hsgameparser.achievements.GameType.RANKED;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_CARD_ATTRIBUTE_VALUE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_CARD_TEXT_NUMBER_OF_WORDS;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_CARD_TEXT_VALUE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_CLASSIC;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_MECHANIC;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_NO_CARD_WITH_LETTER_IN_NAME;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_NUMBER_OF_MINIONS;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_RARITY;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_WON;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.RANKED_FORMAT_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.RANKED_MIN_RANK;
import static com.zerotoheroes.hsgameparser.achievements.generator.GeneralHelper.buildObjectMapper;
import static org.assertj.core.util.Lists.newArrayList;

public class DeckbuildingAchievements implements WithAssertions {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = buildObjectMapper();
    }

    @Test
    public void generate_achievements() throws Exception {
        List<RawAchievement> classic = clasics();
        List<RawAchievement> epic = epics();
        List<RawAchievement> lifesteals = lifesteals();
        List<RawAchievement> voids = voids();
        List<RawAchievement> quiets = quiets();
        List<RawAchievement> thousandCuts = thousandCuts();
        List<RawAchievement> fragileNatures = fragileNatures();
        List<RawAchievement> summoners = summoners();
        List<RawAchievement> randoms = randoms();
        List<RawAchievement> simples = simples();
        List<RawAchievement> complexes = complexes();
        List<RawAchievement> result =
                Stream.of(
                        classic,
                        epic,
                        lifesteals,
                        voids,
                        quiets,
                        thousandCuts,
                        fragileNatures,
                        summoners,
                        randoms,
                        simples,
                        complexes)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(types);
        List<String> serializedAchievements = result.stream()
                .map(GeneralHelper::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> clasics() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> classic(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement classic(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("deckbuilding_win_classic_" + minimumRank)
                .type("deckbuilding_win_classic")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Classic")
                .displayName("Achievement completed: Classic (rank " + minimumRank + ")")
                .displayCardId("CS2_127")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with a deck containing only Classic and Basic cards in Ranked Standard")
                .completedText("Completed at rank " + minimumRank + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DECK_CLASSIC).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> epics() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> epic(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement epic(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("deckbuilding_win_epic_" + minimumRank)
                .type("deckbuilding_win_epic")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Epic")
                .displayName("Achievement completed: Epic (rank " + minimumRank + ")")
                .displayCardId("TB_KTRAF_HP_RAF4")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with a deck containing only epic cards in Ranked Standard")
                .completedText("Completed at rank " + minimumRank + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DECK_RARITY).values(newArrayList("30", "AT_LEAST", "epic")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> lifesteals() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> lifesteal(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement lifesteal(int minimumRank, boolean isRoot) {
        int minLifestealCards = 12;
        return RawAchievement.builder()
                .id("deckbuilding_win_lifesteal_" + minimumRank)
                .type("deckbuilding_win_lifesteal")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Lifesteal")
                .displayName("Achievement completed: Lifesteal (rank " + minimumRank + ")")
                .displayCardId("DAL_047")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with a deck containing at least " + minLifestealCards + " cards with lifesteal in Ranked Standard")
                .completedText("Completed at rank " + minimumRank + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DECK_MECHANIC).values(newArrayList("" + minLifestealCards, "LIFESTEAL", "AT_LEAST")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> voids() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> theVoid(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement theVoid(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("deckbuilding_win_the_void_" + minimumRank)
                .type("deckbuilding_win_the_void")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("The Void")
                .displayName("Achievement completed: The Void (rank " + minimumRank + ")")
                .displayCardId("ULD_134")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with a deck containing no cards with the letter \"e\" in its (English) name in Ranked Standard")
                .completedText("Completed at rank " + minimumRank + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DECK_NO_CARD_WITH_LETTER_IN_NAME).values(newArrayList("e")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> quiets() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> quiet(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement quiet(int minimumRank, boolean isRoot) {
        int minNumberOfMinions = 15;
        return RawAchievement.builder()
                .id("deckbuilding_win_so_quiet_" + minimumRank)
                .type("deckbuilding_win_so_quiet")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("So quiet")
                .displayName("Achievement completed: So Quiet (rank " + minimumRank + ")")
                .displayCardId("LOE_077")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with a deck containing at least " + minNumberOfMinions + " minions and no battlecry or deathrattle card in Ranked Standard")
                .completedText("Completed at rank " + minimumRank + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DECK_NUMBER_OF_MINIONS).values(newArrayList("" + minNumberOfMinions, "AT_LEAST")).build(),
                        Requirement.builder().type(DECK_MECHANIC).values(newArrayList("" + 0, "BATTLECRY", "AT_MOST")).build(),
                        Requirement.builder().type(DECK_MECHANIC).values(newArrayList("" + 0, "DEATHRATTLE", "AT_MOST")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> thousandCuts() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> thousandCut(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement thousandCut(int minimumRank, boolean isRoot) {
        int minNumberOfMinions = 15;
        return RawAchievement.builder()
                .id("deckbuilding_win_thousand_cuts_" + minimumRank)
                .type("deckbuilding_win_thousand_cuts")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Death by a Thousand Cuts")
                .displayName("Achievement completed: Death by a Thousand Cuts (rank " + minimumRank + ")")
                .displayCardId("DAL_773")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with a deck containing at least " + minNumberOfMinions + " minions with 1 attack or less and no spell in Ranked Standard")
                .completedText("Completed at rank " + minimumRank + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DECK_CARD_ATTRIBUTE_VALUE).values(newArrayList("" + minNumberOfMinions, "AT_LEAST", "attack", "1", "AT_MOST")).build(),
                        Requirement.builder().type(DECK_TYPE).values(newArrayList("" + 0, "SPELL", "AT_MOST")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> fragileNatures() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> fragileNature(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement fragileNature(int minimumRank, boolean isRoot) {
        int minNumberOfMinions = 15;
        return RawAchievement.builder()
                .id("deckbuilding_win_fragile_nature_" + minimumRank)
                .type("deckbuilding_win_fragile_nature")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Fragile Nature")
                .displayName("Achievement completed: Fragile Nature (rank " + minimumRank + ")")
                .displayCardId("GIL_665")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with a deck containing at least " + minNumberOfMinions + " minions with 1 health and no spell in Ranked Standard")
                .completedText("Completed at rank " + minimumRank + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DECK_CARD_ATTRIBUTE_VALUE).values(newArrayList("" + minNumberOfMinions, "AT_LEAST", "health", "1", "AT_MOST")).build(),
                        Requirement.builder().type(DECK_TYPE).values(newArrayList("" + 0, "SPELL", "AT_MOST")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> summoners() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> summoner(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement summoner(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("deckbuilding_win_summoner_" + minimumRank)
                .type("deckbuilding_win_summoner")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Summoner")
                .displayName("Achievement completed: Summoner (rank " + minimumRank + ")")
                .displayCardId("DAL_575")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with a deck containing only cards with the word \"Summon\" in their text or name in Ranked Standard")
                .completedText("Completed at rank " + minimumRank + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DECK_CARD_TEXT_VALUE).values(newArrayList("" + 30, "AT_LEAST", "summon", "CONTAINS")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> randoms() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> random(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement random(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("deckbuilding_win_random_" + minimumRank)
                .type("deckbuilding_win_random")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("My Own Casino")
                .displayName("Achievement completed: My Own Casino (rank " + minimumRank + ")")
                .displayCardId("KAR_009")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with a deck containing only cards with the word \"random\" in their text or name in Ranked Standard")
                .completedText("Completed at rank " + minimumRank + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DECK_CARD_TEXT_VALUE).values(newArrayList("" + 30, "AT_LEAST", "random", "CONTAINS")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> simples() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> simple(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement simple(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("deckbuilding_win_simple_" + minimumRank)
                .type("deckbuilding_win_simple")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Keeping it Simple")
                .displayName("Achievement completed: Keeping it Simple (rank " + minimumRank + ")")
                .displayCardId("CS2_182")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with a deck containing only cards with at most 3 words in their text in Ranked Standard")
                .completedText("Completed at rank " + minimumRank + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DECK_CARD_TEXT_NUMBER_OF_WORDS).values(newArrayList("" + 30, "AT_LEAST", "3", "AT_MOST")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> complexes() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> complex(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement complex(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("deckbuilding_win_complex_" + minimumRank)
                .type("deckbuilding_win_complex")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Complexity")
                .displayName("Achievement completed: Complexity (rank " + minimumRank + ")")
                .displayCardId("CS2_233")
                .displayCardType("spell")
                .difficulty("rare")
                .emptyText("Win one game with a deck containing only cards with at leat 8 words in their text in Ranked Standard")
                .completedText("Completed at rank " + minimumRank + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DECK_CARD_TEXT_NUMBER_OF_WORDS).values(newArrayList("" + 30, "AT_LEAST", "8", "AT_LEAST")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }
}
