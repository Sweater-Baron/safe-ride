

# Edit to use most recent version of Pyvenv on platform  
PYVENV = python3 -m venv

install:
	$(PYVENV) env
	(.  env/bin/activate; pip install -r requirements.txt)

dist:
	pip freeze > requirements.txt