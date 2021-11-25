package it.teamdigitale.ndc.harvester.model;

import it.teamdigitale.ndc.harvester.model.exception.InvalidModelException;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class SemanticAssetModelFactoryTest {

    private final SemanticAssetModelFactory factory = new SemanticAssetModelFactory();

    @Test
    void canBuildControlledVocabularyModel() {
        String ttlFile = "src/test/resources/testdata/cv.ttl";

        ControlledVocabularyModel model = factory.createControlledVocabulary(ttlFile);

        Resource resource = model.getMainResource();

        assertThat(resource).isNotNull();
        assertThat(resource.toString()).isEqualTo(
                "https://w3id.org/italia/controlled-vocabulary/classifications-for-accommodation-facilities/accommodation-star-rating");
    }

    @Test
    void canBuildOntologyModel() {
        String ttlFile = "src/test/resources/testdata/onto.ttl";

        OntologyModel model = factory.createOntology(ttlFile);

        Resource resource = model.getMainResource();

        assertThat(resource.toString()).isEqualTo("https://w3id.org/italia/onto/CulturalHeritage");
    }

    @Test
    void shouldFailForInvalidControlledVocabularyModel() {
        assertThatThrownBy(() -> factory.createControlledVocabulary("src/main/resources/application.properties"))
                .isInstanceOf(InvalidModelException.class);
    }

    @Test
    void shouldFailForInvalidOntologyModel() {
        assertThatThrownBy(() -> factory.createOntology("src/main/resources/application.properties"))
                .isInstanceOf(InvalidModelException.class);
    }
}