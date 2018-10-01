import com.leikr.core.LeikrEngine;
//Leikr Schmup game. With huge thanks to the help of this tutorial
// https://ztiromoritz.github.io/pico-8-shooter/
class Lschmup extends LeikrEngine{
	
	def p = [:];
	def bullets = [];
	def clouds = [];	
	def enemies = [];
	def score = 0;
	def wave = 1;

	Random rand;

    def void create(){

		p.sprite = 0;
		p.y = 100;
		p.x = 5;
		p.xv = 0;
		p.yv = 0;

		rand = new Random();

		// populate clouds
		for(int i = 0; i < 200; i++){
			def temp = [:];
			temp.x = rand.nextInt((getScreenWidth() - 1)+1)+1;
			temp.y = rand.nextInt((getScreenHeight() - 1)+1)+1;
			temp.s = rand.nextInt((4-1)+1)+1;
			clouds.add(temp);
		}
	
		
		def e = [:];
		e.x = getScreenWidth();
		e.y = rand.nextInt(((getScreenHeight()-16) - 16)+1)+16;
		e.s = rand.nextInt((4-1)+1)+1;
		e.sprite = 16;
		e.dead = false;
		enemies.add(e);
    }

	void addWave(){
		wave++;
		for(int i = 0; i < wave; i++){
			def e = [:];
				e.x = getScreenWidth();
				e.y = rand.nextInt(((getScreenHeight()-16) - 16)+1)+16;
				e.s = rand.nextInt((4-1)+1)+1;
				e.sprite = 16;
				e.dead = false;
				enemies.add(e);
		}
	}
		
	void pbullets(){
		if(key("space")){
			def b = [:];
			b.x = p.x;
			b.y = p.y;
			b.xv = 4;
			b.hit = false;
			bullets.add(b);
		}
	}

	void pmoving(){

		p.yv = 0;
		p.sprite = 0;
		if(key("up") && p.y < getScreenHeight() - 12){
			p.sprite = 1;
			p.yv = 2;
		}
		if(key("down") && p.y > 6){
			p.sprite = 2;
			p.yv = -2;
		}

		p.xv = 0;
		if(key("right") && p.x < getScreenWidth()-12){
			p.xv = 2;
		}
		if(key("left") && p.x > 6){
			p.xv = -2;
		}
		
		p.y += p.yv;
		p.x += p.xv;
		
	}

	void renderClouds(){
		clouds.each{
			rect(it.x, it.y, 1, 1,0, "filled");
			it.x -= it.s;
			if(it.x < 0){
				it.x = getScreenWidth();
			}
		}
	}

	void renderEnemies(){
		enemies.each{
			it.x -= it.s;
			sprite(it.sprite, it.x, it.y);
			if(it.x < 0){
				it.x = getScreenWidth();
				it.y = rand.nextInt(((getScreenHeight()-16) - 16)+1)+16;
				it.s = rand.nextInt((2-1)+1)+1;
			}
		}
	}

	void hit(){
		for(def e in enemies){
			for(def b in bullets){
				if(b.x >= e.x && b.x <= e.x+8 && b.y >= e.y && b.y <= e.y+8){
					b.hit = true;
					e.dead = true;
					score += 10;
				}
			}
		}
		bullets.removeAll{it.hit == true;}
		enemies.removeAll{it.dead == true;}
	}


    def void render(float dt){
		//bg
		rect(0, 0, getScreenWidth(), getScreenHeight(), 6, "filled");
		drawText("Score: "+score, 1, getScreenHeight() - 8, 2);
		hit();
		renderClouds();
	
		pmoving();
		pbullets();
		sprite(p.sprite, p.x, p.y);// draw player    
		renderEnemies();
		// draw bullets
		bullets.removeAll{ it.x > getScreenWidth();}
		bullets.each{
			it.x += 4;
			sprite(6, it.x, it.y);			
		}

		if(enemies.size() == 0){
			addWave();
		}
    }
}