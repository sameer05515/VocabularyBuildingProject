package com.prem.vocab.build.proj;

public enum VocabBuildConstants implements XMLBasicMethodsRequired {
	ROOT("vocab-config", VocabBuildConstants.Type.NODE), WORD_LIST("word-list", VocabBuildConstants.Type.NODE),
	MY_WORD("myword", VocabBuildConstants.Type.NODE);

	private String name;
	private VocabBuildConstants.Type type;

	private VocabBuildConstants(String name, VocabBuildConstants.Type type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public VocabBuildConstants.Type getType() {
		return type;
	}


	public enum Type{
		NODE;
	}

	public enum Meanings implements XMLBasicMethodsRequired{
		//node,MEANING;
		node("vocab-config", VocabBuildConstants.Type.NODE);

		private String name;
		private VocabBuildConstants.Type type;

		private Meanings(String name, VocabBuildConstants.Type type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public VocabBuildConstants.Type getType() {
			return type;
		}


	}

	public enum Examples implements XMLBasicMethodsRequired{

		node("vocab-config", VocabBuildConstants.Type.NODE);

		private String name;
		private VocabBuildConstants.Type type;

		private Examples(String name, VocabBuildConstants.Type type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public VocabBuildConstants.Type getType() {
			return type;
		}

	}

	public enum Word implements XMLBasicMethodsRequired{
		node("vocab-config", VocabBuildConstants.Type.NODE);

		private String name;
		private VocabBuildConstants.Type type;

		private Word(String name, VocabBuildConstants.Type type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public VocabBuildConstants.Type getType() {
			return type;
		}

	}
}
