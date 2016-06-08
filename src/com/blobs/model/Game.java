package com.blobs.model;
import java.util.Timer;

import com.blobs.view.IView;

public class Game implements IModel{
	
	private Timer spawnTimer;
	private Timer gameTimer;
	private Gamefield field;
	private int score;
	private IView view;
	private RankingManager manager;
	
	public Game(IView view) {
		this.view = view;
		this.manager = new RankingManager();
		reset();
	}
	
	private void reset() {
		this.field = new Gamefield(3,3);
		this.gameTimer = new Timer();
		this.spawnTimer = new Timer();
		this.score = 0;
	}
	
	public void killBlob(Blob blob) {
		this.score += blob.getScore();
		this.field.removeBlob(blob.getId());
		this.view.hideBlob(blob);
		this.view.updateScore(this.score);
	}
	
	public void gameOver() {
		System.out.println("Game Over");
		this.gameTimer.cancel();
		this.spawnTimer.cancel();
		this.view.gameOver();
	}
	
	public void addBlob() {
		Blob blob = this.field.addBlob();
		this.view.showBlob(blob);
	}

	@Override
	public void startGame() {
		this.view.gameStarted();
		this.gameTimer.scheduleAtFixedRate(new CheckGameTask(this, this.field), 50, 50);
		this.spawnTimer.schedule(new NewBlobTask(this, this.spawnTimer, 2000), 2000);
	}

	@Override
	public void restartGame() {
		this.reset();
		this.startGame();
	}

	@Override
	public void submitHighscore(String name) {
		this.manager.addRanking(new Ranking(name, this.score));
		this.view.showLeaderboards(this.manager.getRankings());
	}
}
