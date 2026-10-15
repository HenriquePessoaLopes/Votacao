// Aplica o tema salvo antes da página ser desenhada, para evitar "piscar" entre claro e escuro.
(function () {
  try {
    var tema = localStorage.getItem('tema');
    if (tema === 'light' || tema === 'dark') {
      document.documentElement.setAttribute('data-theme', tema);
    }
  } catch (e) {
    // localStorage indisponível: segue o tema do sistema
  }
})();
