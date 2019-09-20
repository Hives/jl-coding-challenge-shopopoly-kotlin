package gameLedger

import Board
import DevelopmentType
import GBP
import GameLedger
import Group
import Location
import Player
import assertk.assertThat
import assertk.assertions.isEqualTo
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

    @Test
    fun `returns emptyList when no purchases have been made`() {
        GameLedger.initialise()
        val actual = GameLedger.getPlayersLocations(player1)
        assertThat(actual).isEqualTo(emptyList<Location>())
    }

    @Test
    fun `returns factory purchased by player`() {
        GameLedger.initialise()
        GameLedger.purchaseLocation(player1, factory)
        println(factory)
        assertThat(GameLedger.getPlayersLocations(player1)).isEqualTo(listOf(factory))
    }

    @Test
    fun `returns retail location purchased by player`() {
        GameLedger.initialise()
        GameLedger.purchaseLocation(player1, oxfordStreet)
        assertThat(GameLedger.getPlayersLocations(player1)).isEqualTo(listOf(oxfordStreet))
    }
}