package br.edu.ufcg.embedded.ise.geneguis;

public class Container {

	private final DomainModel model;

	public Container(DomainModel model){
		if (model == null) {
			throw new ContainerException("The domain model is mandatory");
		}
		
		this.model = model;
	}
	
	public Iterable<EntityType> getEntityTypes() {
		return model.getEntityTypes();
	}

}
