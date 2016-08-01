package br.edu.ufcg.embedded.ise.geneguis.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufcg.embedded.ise.geneguis.backend.TagRule;
import br.edu.ufcg.embedded.ise.geneguis.backend.repository.TagRuleRepository;

@Service
public class TagService {

	@Autowired
	private TagRuleRepository tagRepository;

	public void clear() {
		tagRepository.deleteAll();
	}

	public List<TagRule> getAllTags() {
		return tagRepository.findAll();
	}

	public TagRule saveTagRule(TagRule jpa) {
		return tagRepository.save(jpa);
	}

}