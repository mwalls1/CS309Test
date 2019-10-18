package com.example.demo.lobby;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class lobbyController {
	@Autowired
	private lobbyRepository lobbyRepo;
	
	@GetMapping("getLobbies")
	public List<lobby> getlobbys(){
		return lobbyRepo.findAll();
	}
	
	@GetMapping("getLobbyByID")
	public String getXbyID(Integer id) {
		Optional<lobby> l = lobbyRepo.findById(id);
		lobby l1 = l.get();
		return l1.getPlayer1() + " " + l1.getPlayer2() + " " + l1.getPlayer3() + " " + l1.getPlayer4();
	}
	
	@PostMapping("/newLobby")
	public String newLobby(Integer id) {
		lobby l = new lobby();
		l.setId(id);
		lobbyRepo.save(l);
		return "New Lobby made with id:" + l.getId();
	}
	
	@PostMapping("/updatePlayer")
	public String updatePos(Integer id, Integer player, Integer playerId) {
		Optional<lobby> l = lobbyRepo.findById(id);
		lobby l1 = l.get();
		switch(player) {
		case(1):
			l1.setPlayer1(playerId);
			break;
		case(2):
			l1.setPlayer2(playerId);
			break;
		case(3):
			l1.setPlayer3(playerId);
			break;
		case(4):
			l1.setPlayer4(playerId);
		 	break;
		}
		lobbyRepo.save(l1);
		return "Updated player " + player + " to id: " + playerId;
	}
	
	@PutMapping("/cleanLobbies")
	public String cleanLobbies() {
		List<lobby> lobbies = lobbyRepo.findAll();
		for (lobby l : lobbies) {
			if ((l.getPlayer1() == null) || (l.getPlayer1() == 0)) {
				lobbyRepo.deleteById(l.getId());
			}
		}
		
		return "Lobbies Cleaned";
	}
}
