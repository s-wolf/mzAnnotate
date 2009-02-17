CMLSPECT=../cml/ci600531a_Supplemental/schema.xsd

all: validate

clean:
	@rm *~

validate: 
	@echo "Validating against schema..."
	@xmllint --noout --schema ${CMLSPECT} sumformulareactionspectrum.cml
	@echo "Validating against schematron..."
	@xmllint --noout --schematron mzAnnot.schematron sumformulareactionspectrum.cml
