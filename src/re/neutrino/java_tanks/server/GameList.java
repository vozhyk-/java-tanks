package re.neutrino.java_tanks.server;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.debug.DebugStream;
import re.neutrino.java_tanks.types.JoinReply;

public class GameList extends ArrayList<Game> {
	/**
	 *
	 */
	private static final long serialVersionUID = 3356190216841107810L;

	private Config config;
	private DebugStream debug;

	Random rand = new Random();

	public GameList(Config config, DebugStream debug) {
		this.config = config;
		this.debug = debug;
	}

	public JoinReply tryJoin(String nickname) {
		if (isEmpty())
			return JoinReply.NoGames;

		Stream<Game> s = stream();

		return
				s.filter(g -> g.findClientByNickname(nickname).isPresent())
				.findAny()
			.map(Optional::of).orElse(
				findRandom(g -> !g.isStarted()))
			.map(g -> g.tryJoin(nickname))
			.orElse(JoinReply.GameInProgress);
	}

	Optional<Game> findRandom(Predicate<Game> pred) {
		long count = stream().filter(pred).count();
		if (count == 0)
			return Optional.empty();

		long i = rand.longs(1, 0, count).findAny().getAsLong();

		// Searching a second time?
		return stream().filter(pred).skip(i).findFirst();
	}

	public Game addNewGame() {
		Game result = new Game(config, debug);

		add(result);
		return result;
	}
}
