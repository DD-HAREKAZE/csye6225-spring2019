package io.webApp.springbootstarter.register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.springframework.stereotype.Service;

@Service
public class registerService {
	
	TableCreation tableCreation;

	
	public void dbCreation() {
		tableCreation.createDB();
	}
}
