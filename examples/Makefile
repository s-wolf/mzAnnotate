CMLSPECT=../cml/ci600531a_Supplemental/schema.xsd
SCHEMATRON=mzAnnot.schematron 

EXAMPLES=sumformulareactionspectrum.cml \
         reactionSchemeMoleculeSet.cml \
         psicvExample.cml

all: lint validate

clean:
	@rm *~

lint: $(EXAMPLES:%.cml=%.lint)
validate: $(EXAMPLES:%.cml=%.linttron)

%.lint: %.cml
	@echo -n "Lint'ing against schema... "
	@xmllint --noout --schema ${CMLSPECT} $<

%.linttron: %.cml
	@echo -n "Validating against schematron... "
	@xmllint --noout --schematron ${SCHEMATRON} $<
