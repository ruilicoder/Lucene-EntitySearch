package twitter;


public class TwitterStatus {
	public static final String SIMPLE_TWITTER_STATUS = "SIMPLE";
	public static final String COMPLEX_TWITTER_STATUS = "COMPLEX";
	public static final String BEGIN = "*****";
	private String status;
	private String createAt;
	private String text;
	private long id;
	private boolean isReplyTo;
	private boolean isFaverate;	
	private long userID;
	private	long numberFollower;
	private long numberFriends;
	private long numberFaverats;
	
	
	public TwitterStatus(long id, String text, String createAt,	boolean isReplyTo) {
		// this.userName = userName;
		this.status = SIMPLE_TWITTER_STATUS;
		this.createAt = createAt;
		this.text = text.replaceAll("\r\n", " ");
		this.text = this.text.replaceAll("\n", " ");
		this.text = this.text.replaceAll("\r", " ");
	
		this.isReplyTo = isReplyTo;
		this.id = id;	
	}
	
	public TwitterStatus(long id, String  text, String createAt, boolean isReplyTo, boolean isF, long userID, long nfo, long nfr, long nfa) {
		this.status = COMPLEX_TWITTER_STATUS;		 
		this.id = id;
		 this.text = text.replaceAll("\r\n", " ");
		 this.text = this.text.replaceAll("\n", " ");
		 this.text = this.text.replaceAll("\r", " "); 
		 this.createAt = createAt;
		 this.isReplyTo = isReplyTo;
		 this.isFaverate = isF;
		 this.userID  = userID;
		 this.numberFollower = nfo;
		 this.numberFriends = nfr;
		 this.numberFaverats =  nfa;
	}
	
	public String convertToString() {
		//if ()
		StringBuilder  content = new StringBuilder();
		content.append(id + "\n");
		content.append(text + "\n");
		content.append(createAt + "\n");
		content.append(isReplyTo + "\n");
		if (this.status.compareTo(this.COMPLEX_TWITTER_STATUS) == 0) {
			content.append(isFaverate + "\n");	
			content.append(userID + "\n");
			content.append(numberFollower + "\n");
			content.append(numberFriends + "\n");
			content.append(numberFaverats + "\n");		
		}
		return content.toString();
	}
	public String getCreatedAt() {
		return createAt;
	}

	public String getText() {
		return text;
	}
	public String parseText() {
		//text.replace("http://", newChar)
		String content = text;
		content = content.replaceAll("@[\\w]+", " ");
		content = content.replaceAll("http://([\\w]+\\.)+[\\w]+(/[\\w]*)*", "");
		return content;
	}
	public boolean isEnglishChar() {
		boolean label = true;
		for (int k=0 ; k<text.length(); k++) {				
			if (text.charAt(k)<128) {
		      //System.out.print(content.charAt(k));
			} else {
				label = false;
			}
		}
		return label;	
	}
	public int  getLength() {
		return text.split(" ").length;
	}
	public long getId() {
		return id;
	}

	public boolean getIsReplyTo() {
		return isReplyTo;
	}
	
	public boolean getIsFaveroate() {
		return this.isFaverate;
	}
	
	public long getUserID() {
		return this.userID;
	}
	public long getNumFollowers() {
		return this.numberFollower;
	}
	public long getNumFavorates() {
		return this.numberFaverats;
	}
	public long getNumFriends() {
		return this.numberFriends;
	}
	public static void main(String[] args ) {
		//String test = "We Will Blow Your Mind with this Affiliate Program: http://bit.ly/49Qhus aabafdad @kylemackenzie @Riding4aReason @medicalcrisis @thechessworld";
		//System.out.println(t);
	}
}
