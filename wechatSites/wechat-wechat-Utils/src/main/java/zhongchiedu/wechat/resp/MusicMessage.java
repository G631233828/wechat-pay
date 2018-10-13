package zhongchiedu.wechat.resp;


/**
 * 音乐消息
 * 
 * @author fliay	
 * @date 2018年8月13日 11:06:44
 */
public class MusicMessage extends BaseMessage {
	// 音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}