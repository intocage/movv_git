package com.movv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.movv.entity.Reservation;
import com.movv.repository.ReservationRepository;

@Controller
public class ReservationController {
	@Autowired
	public ReservationRepository repository;
	
	@GetMapping("/")
	public String welcome(Model model) {
		
		List<Reservation> list= repository.findAll();
		
		model.addAttribute("list",list);
		
		return "Main";
	}
	@RequestMapping("/Main")
	public String Main(Model model) {
		
		List<Reservation> list= repository.findAll();
		
		model.addAttribute("list",list);
		
		return "Main";
	}

}
