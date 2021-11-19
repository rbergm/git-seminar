package de.tudresden.geo.gitseminar.util;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlidingWindowTests {

	private final static Logger log = LoggerFactory.getLogger(SlidingWindowTests.class);

	private final List<Integer> evenList = List.of(1, 2, 3, 4, 5, 6);
	private final List<Integer> oddList = List.of(1, 2, 3, 4, 5, 6, 7);

	@Test
	public void fullySlicesEvenListWithEvenWindow() {
		var result = SlidingWindow.over(evenList, 2);
		log.debug("Even/Even {}", result);
	}

	@Test
	public void fullySlicesEvenListWithOddWindow() {
		var result = SlidingWindow.over(evenList, 3);
		log.debug("Even/Odd {}", result);
	}

	@Test
	public void fullySlicesOddListWithEvenWindow() {
		var result = SlidingWindow.over(oddList, 2);
		log.debug("Odd/Even {}", result);
	}


	@Test
	public void fullySlicesOddListWithOddWindow() {
		var result = SlidingWindow.over(oddList, 3);
		log.debug("Odd/Odd {}", result);
	}

}
