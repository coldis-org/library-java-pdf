package org.coldis.library.test.dto;

import java.util.List;
import java.util.Map;

import org.coldis.library.test.dto.dto.DtoTestObject2Dto;
import org.coldis.library.test.dto.dto.DtoTestObjectDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * DTO generator test.
 */
public class DtoGeneratorTest {

	/**
	 * Simples JSON object test data.
	 */
	private static final DtoTestObjectDto[] TEST_DATA = { new DtoTestObjectDto(10L, new DtoTestObject2Dto(1L, "test1"),
			List.of(new DtoTestObject2Dto(2L, "test2"), new DtoTestObject2Dto(3L, "test3")),
			Map.of("id", 4L, "test", "test4"),
			new DtoTestObject2Dto[] { new DtoTestObject2Dto(5L, "test5"), new DtoTestObject2Dto(6L, "test6") }, 1,
			new int[] { 2, 3, 4 }, 5) };

	/**
	 * Object mapper.
	 */
	private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	/**
	 * Tests the DTO creation.
	 *
	 * @throws Exception If the test does not succeed.
	 */
	@Test
	public void testDtoCreation() throws Exception {
		// For each test data.
		for (DtoTestObjectDto originalDto : TEST_DATA) {
			// Converts the DTO to the original object and back.
			DtoTestObject originalObject = objectMapper.convertValue(originalDto, DtoTestObject.class);
			DtoTestObjectDto reconvertedDto = objectMapper.convertValue(originalObject, DtoTestObjectDto.class);
			// The DTO should remain the same.
			Assertions.assertEquals(originalDto, reconvertedDto);
			// Asserts that the attribute not used in comparison is the same and it does not
			// affect equality.
			Assertions.assertEquals(originalDto.getTest9(), reconvertedDto.getTest9());
			reconvertedDto.setTest9(8);
			Assertions.assertNotEquals(originalDto.getTest9(), reconvertedDto.getTest9());
			Assertions.assertEquals(originalDto, reconvertedDto);

		}
	}

}
