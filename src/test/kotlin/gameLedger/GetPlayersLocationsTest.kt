package gameLedger

import Board
import DevelopmentType
import GBP
import GameLedger
import Group
import Location
import Player
import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetPlayersLocationsTest {
    val board = Board(emptyList()) // should mock this!
    val player1 = Player("Paul", board)
    val player2 = Player("Yvonne", board)
    val oxfordStreet = Location.Retail(
        name = "Oxford Street",
        cost = GBP(1000),
        group = Group.BLUE,
        undeveloped = DevelopmentType.RentOnly(GBP(10)),
        ministore = DevelopmentType.CostAndRent(GBP(200), GBP(20)),
        supermarket = DevelopmentType.CostAndRent(GBP(300), GBP(30)),
        megastore = DevelopmentType.CostAndRent(GBP(400), GBP(40))
    )
    val factory = Location.FactoryOrWarehouse("OriginPark")

    @BeforeEach
    fun `reset game ledger`() {
        GameLedger.initialise()
    }

    @Test
    fun `returns emptyList when no purchases have been made`() {
        val actual = GameLedger.getPlayersLocations(player1)
        assertThat(actual).isEqualTo(emptyList<Location>())
    }

    @Test
    fun `returns factory purchased by player`() {
        GameLedger.purchaseLocation(player1, factory)
        println(factory)
        assertThat(GameLedger.getPlayersLocations(player1)).isEqualTo(listOf(factory))
    }

    @Test
    fun `does not return factory purchased by another player`() {
        GameLedger.purchaseLocation(player2, factory)
        println(factory)
        assertThat(GameLedger.getPlayersLocations(player1)).isEmpty()
    }

    @Test
    fun `returns retail location purchased by player`() {
        GameLedger.purchaseLocation(player1, oxfordStreet)
        assertThat(GameLedger.getPlayersLocations(player1)).isEqualTo(listOf(oxfordStreet))
    }

    @Test
    fun `returns retail location and factory purchased by player`() {
        GameLedger.purchaseLocation(player1, oxfordStreet)
        GameLedger.purchaseLocation(player1, factory)
        assertThat(GameLedger.getPlayersLocations(player1)).containsOnly(oxfordStreet,factory)
    }

    @Test
    fun `does not return a location if another player has bought it`() {
        GameLedger.purchaseLocation(player1, oxfordStreet)
        GameLedger.purchaseLocation(player2, oxfordStreet)
        assertThat(GameLedger.getPlayersLocations(player1)).isEmpty()
    }

    @Test
    fun `return a location if player has bought it from another player`() {
        GameLedger.purchaseLocation(player1, oxfordStreet)
        GameLedger.purchaseLocation(player2, oxfordStreet)
        assertThat(GameLedger.getPlayersLocations(player2)).isEqualTo(listOf(oxfordStreet))
    }
}
