package com.roulette.server.service.error

import com.roulette.server.dto.game.GameDto
import com.roulette.server.dto.player.PlayerDto

object game {

  sealed trait GameValidationError {
    def message: String
  }

  object GameValidationError {
    final case class GameNotFound(game: GameDto) extends GameValidationError {
      override def message: String =
        s"The game with id `${game.id}` is not found"
    }

    final case class InvalidGameBets(game: GameDto)
        extends GameValidationError {
      override def message: String = "Invalid bets are specified. " +
        s"Minimal `${game.minBetAmount}` should be lower than maximum `${game.maxBetAmount}`."
    }

    final case class InvalidGameStatus(game: GameDto)
        extends GameValidationError {
      override def message: String = s"Status `${game.status}` is invalid."
    }

    final case object PlayerCountExceed extends GameValidationError {
      override def message: String = "No available seats in the lobby."
    }

    final case class PlayerBetIsInvalid(
        players: List[PlayerDto],
        minBetAmount: Int,
        maxBetAmount: Int
    ) extends GameValidationError {
      override def message: String = {
        val lobbyBets =
          s"The minimum and maximum bets of the lobby are: `$minBetAmount`, $maxBetAmount."
        val playersBets = players.foldLeft("\n")((acc, player) =>
          s"$acc player `${player.name}` bet is ${player.name}\n"
        )

        lobbyBets + playersBets
      }
    }
  }
}
