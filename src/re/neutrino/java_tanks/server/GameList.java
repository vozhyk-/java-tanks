package re.neutrino.java_tanks.server;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import re.neutrino.java_tanks.types.JoinReply;

public class GameList extends ArrayList<Game> {
	/**
	 *
	 */
	private static final long serialVersionUID = 3356190216841107810L;

	Random rand = new Random();

	public JoinReply tryJoin(String nickname) {
		if (isEmpty())
			return JoinReply.NoGames;

		Stream<Game> s = stream();

		return
				s.filter(g -> g.clientCanRejoin(nickname)).findAny()
			.map(Optional::of).orElse(
				findRandom(g -> !g.isStarted()))
			.map(g -> g.tryJoin(nickname))
			.orElse(JoinReply.GameInProgress);
	}

	Optional<Game> findRandom(Predicate<Game> pred) {
		long count = stream().filter(pred).count();
		long i = rand.longs(1, 0, count).findAny().getAsLong();

		// Searching a second time?
		return stream().filter(pred).skip(i).findFirst();
	}
}
