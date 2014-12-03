package jp.co.worksap.sample.dto;

public class JobSelectHeader extends JobDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5747818372977548126L;
	public JobSelectHeader(){
		super();
		this.setId("Id");
		this.setTitle("title");
		ClientDto client = new ClientDto();
		client.setName("client");
		this.setClient(client);
		
		
	}
	@Override
	public String getProgressLabel(){
		return "progress";
	}
	@Override
	public String getNoResponseString(){
		return "no response";
	}
	@Override
	public String getRefusedString(){
		return "refused";
	}
	@Override
	public String getStatusLabel(){
		return "status";
	}
	@Override
	public String getAmountString(){
		return "amount";
	}
	

}
