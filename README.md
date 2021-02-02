# Vision'n Art :eyes: :art:

<h1>Table de matières</h1>
<ul>
	<li><a href="#cible">Les Cibles :dart:</a></li>
	<li><a href="#description">Description :speech_balloon:</a></li>
	<li><a href="#specTech">Specifications Techniques :gear:</a></li>
	<li><a href="#documentation">Documentation :memo:</a></li>
</ul>

<div id='cible'/>  
<h1>Les Cibles :dart:</h1>
<h2>Les espaces culturels :european_castle:</h2>
<p>Les espaces culturels sont présentés comme étant l’ensemble des personnes morales
	gérant un espace culturel un ensemble d’œuvres. Ces espaces peuvent être des partenaires
	directes avec la société Vision’Art comme des espaces culturels qui souhaite connaitre les
	préférences de ses visiteurs ou encore des statistiques démographiques de ces visiteurs ou
potentiel visiteur.</p>
<h2>Les personnes portant un intérêt à une œuvre :mag_right:</h2>
<p>Pour l’utilisateur final de l’application nous avons souhaité laisser la possibilité à
	celui-ci de rechercher des œuvres d’art sans aucune restriction. Nous avons donc décidé d’axer les informations sur l’œuvre en priorité. L’ensemble de fonctionnalités liées à cet
utilisateur sont précisées dans la suite du document.</p>
</div>

<div id='description'>
	<h1>Description :speech_balloon:</h1>
	Ce projet a été réalisé par une équipe de 3 personnes durant une période de 6 mois entre la phase de conception et de terminaison. L'application Visionnart est une solution adaptée à la mobilité permettant de stocker, de partager et d'afficher du contenu informatif en utilisant des technologies avancées comme le machine learning et la reconnaissance d'image.</br>
	La technologie de reconnaissance d’image a été choisie car elle développe un attrait rapide.
	Le projet comprends deux parties : une application mobile faite pour les utilisateurs finaux et un portail web développé sous la forme d'un CMS permettant d'intégrer rapidement les données pour les nouveaux musées. 
</div>


<div id='specTech'>
	<h1>Spécifications techniques :gear:</h1>
	<h2>Frameworks & Langages utilisés</h2>
	<h3> Kotlin :rocket:</h3>
	<p>
		<img src='https://firebasestorage.googleapis.com/v0/b/visonnart.appspot.com/o/Img%2Fkotlin.png?alt=media&token=73699ebc-e445-46f6-8aa1-92cb86701c9c'/>
	</p>
	<h3>Firebase :fire:</h3>
	<p>
		Firebase est un ensemble de services d'hébergement pour n'importe quel type d'application. Il propose d'héberger en NoSQL et en temps réel des bases de données, du contenu, de l'authentification sociale (Google, Facebook, Twitter et GitHub), et des notifications, ou encore des services, tel que par exemple un serveur de communication temps réel. </br></br>
		Cloud FireStore est une base de données de documents NoSQL rapide, entièrement gérée, sans serveur et native au cloud, qui simplifie le stockage, la synchronisation et l'interrogation de données pour vos applications mobiles, Web et IoT à l'échelle mondiale. Ses bibliothèques client fournissent une synchronisation en direct et une assistance hors ligne, tandis que ses fonctionnalités de sécurité et ses intégrations avec Firebase et Google Cloud Platform (GCP) accélèrent la création d'applications véritablement sans serveur. </br></br>
	Nous avons choisi cette technologie car elle correspondait à nos attentes. Nous avons comparé avec une autre solution FireBase appelé Realtime Database. Voici le comparatif des deux solutions réalisé par Google : https://firebase.google.com/docs/database/rtdb-vs-firestore.</p>
	<p align='center'><img src="https://firebasestorage.googleapis.com/v0/b/visonnart.appspot.com/o/Img%2Ffirebase.png?alt=media&token=a339ffac-0b87-434c-83b4-06d63dba6f18"/></p>
	<h2>APIs et Services</h2>
	<h3>Cloud AutoML :cloud:</h3>
	<p>
	Cloud AutoML est une suite de produits de machine learning qui permet aux développeurs peu expérimentés dans ce domaine d'entraîner des modèles de haute qualité répondant aux besoins spécifiques de leur entreprise. Cette solution bénéficie des fonctionnalités avancées d'apprentissage par transfert de Google et de la technologie NAS (Neural Architecture Search).</br></br>
	Cloud AutoML tire parti de plus de 10 ans consacrés à la technologie propriétaire Google Research pour optimiser les performances de vos modèles de machine learning et améliorer la précision de vos prédictions.</br></br>
	Ce produit permet d’utiliser l'interface utilisateur graphique simple de Cloud AutoML pour entraîner, évaluer, améliorer et déployer des modèles basés sur vos données.</br></br>
	Le service d'ajout manuel d'étiquettes de Google permet à une de vos équipes d'annoter ou d'effacer vos étiquettes. Ainsi, vous avez la certitude que vos modèles sont entraînés à partir de données de haute qualité.
</p>
<p align='center'>
	<img src='https://firebasestorage.googleapis.com/v0/b/visonnart.appspot.com/o/Img%2FAutoML.png?alt=media&token=12dda8cd-0626-4b3d-8efc-f5ad2d9555b6'/>
</p>
    <h3>API Vision IA :eyes:</h3>
    <p>
    L'API Vision de Google Cloud dispose de modèles de machine learning performants pré-entraînés par le biais des API REST et RPC. Attribuez des étiquettes à des images et classez-les rapidement dans des millions de catégories prédéfinies. Vous pourrez ainsi détecter des objets et des visages, lire du texte imprimé ou manuscrit, et intégrer des métadonnées utiles à votre catalogue d'images.
    </p>
    <p align='center'>
      <img src="https://firebasestorage.googleapis.com/v0/b/visonnart.appspot.com/o/Img%2FVisionIA.png?alt=media&token=3740d7d7-92b9-4138-89a3-a1be4bac1ff8">
    </p>
</div>
    <div id='architecture'>
      <h1>Architecture Technique :gear:</h1>
      <p align='center'><img src='https://firebasestorage.googleapis.com/v0/b/visonnart.appspot.com/o/Img%2FArchitecture.png?alt=media&token=fcf344a5-dcd9-4991-a4ca-71646366bbfc'></p>
    </div>
    <div id='documentation'>
      <h1>Documentation :memo:</h1>
    <p>
    Présentation PowerPoint  :orange_book: : <a href='https://firebasestorage.googleapis.com/v0/b/visonnart.appspot.com/o/Img%2FSoutenance%20Finale.pptx?alt=media&token=03c623dc-c740-4b69-af2e-e6d142d96f99'>ici</a><br/>
    Cahier des charges fonctionnel & technique  :blue_book: : <a href='https://firebasestorage.googleapis.com/v0/b/visonnart.appspot.com/o/Img%2FCDC%20-%20DAHLEM%20Romain%20THAN%20Phlippe%20JOUET%20Jerome%20-%20VisionArt.docx?alt=media&token=f5f6c769-64e2-4991-9281-017ef7b20d15'>ici</a></br>
      Vidéo de démonstration  :cinema: : <a href="https://firebasestorage.googleapis.com/v0/b/visonnart.appspot.com/o/Img%2FM%C3%A9dia1.mp4?alt=media&token=5a29e05e-d890-4280-a9f5-67f785b09085" target="_blank">ici</a>
	</p>
    </div>
