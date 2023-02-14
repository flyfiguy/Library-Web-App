package com.springbootlibrary;

import com.springbootlibrary.dao.BookRepository;
import com.springbootlibrary.entity.Book;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
class LibraryApplicationTests {

	@Autowired
	private BookRepository bookRepository;

	@Test
	@Transactional
	public void testAddDelete() {
		final String testTitle = "Fly Fishing Provo River";
		Book book =  bookRepository.save(new Book(testTitle, "Mike Aydelotte", "Dang sweet fishing book.", 10, 10, "Data"));
		int deletedRecords;
		deletedRecords = bookRepository.deleteByTitle(testTitle);
		assertThat("No records deleted", deletedRecords > 0);
	}
	@Test
	void contextLoads() {
	}

}
