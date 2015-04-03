package edu.unlv.searchengine.model;

public class DocumentCosineWeightForTerm implements Comparable<DocumentCosineWeightForTerm>{
	private Long documentId;
	private Double cosineWeight;
	
	public DocumentCosineWeightForTerm(Long documentId, Double cosineWeight) {
		this.documentId = documentId;
		this.cosineWeight = cosineWeight;
	}
	
	public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	public Double getCosineWeight() {
		return cosineWeight;
	}
	public void setCosineWeight(Double cosineWeight) {
		this.cosineWeight = cosineWeight;
	}
	
	@Override
	public String toString() {
		return this.documentId.toString();
		//return this.documentId.toString() + ":" + this.cosineWeight;
	}

	@Override
	public int compareTo(DocumentCosineWeightForTerm o) {
		return this.cosineWeight.compareTo(o.cosineWeight);
	}
	
	
}
