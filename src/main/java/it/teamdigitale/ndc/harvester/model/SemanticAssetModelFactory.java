package it.teamdigitale.ndc.harvester.model;

import it.teamdigitale.ndc.harvester.model.exception.InvalidModelException;
import lombok.RequiredArgsConstructor;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class SemanticAssetModelFactory {
    private interface ModelConstructor<T extends SemanticAssetModel> {
        T build(Model model, String source);
    }

    public ControlledVocabularyModel createControlledVocabulary(String ttlFile) {
        return loadAndBuild(ttlFile, ControlledVocabularyModel::new);
    }

    public OntologyModel createOntology(String ttlFile) {
        return loadAndBuild(ttlFile, OntologyModel::new);
    }

    private <T extends SemanticAssetModel> T loadAndBuild(String source, ModelConstructor<T> c) {
        try {
            Model model = RDFDataMgr.loadModel(source, Lang.TURTLE);
            return c.build(model, source);
        } catch (Exception e) {
            throw new InvalidModelException(format("Cannot load RDF model from '%s'", source), e);
        }
    }
}
