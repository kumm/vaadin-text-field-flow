window.Vaadin.Flow.textConnector = {

  initLazyChange: function(textComponent) {
    if (textComponent.$lazyChange) {
      return;
    }
    textComponent.$lazyChange = {};

    let _timeout;
    let _debouncer;

    const _onInput = function() {
      _timeout && (_debouncer = Polymer.Debouncer.debounce(
        _debouncer,
        Polymer.Async.timeOut.after(_timeout),
        () => textComponent.dispatchEvent(new Event('change'))
      ));
    };

    const _onChange = function() {
      _debouncer && _debouncer.cancel();
    };

    textComponent.$lazyChange.enableLazyChange = function() {
      textComponent.addEventListener('input', _onInput);
      textComponent.addEventListener('change', _onChange);
    };

    textComponent.$lazyChange.disableLazyChange = function() {
      textComponent.removeEventListener('input', _onInput);
      textComponent.removeEventListener('change', _onChange);
      _debouncer && _debouncer.cancel();
    };

    textComponent.$lazyChange.setLazyChangeTimeout = function(timeout) {
      _timeout = timeout;
    }
  },

  initValidation: function(textComponent) {
    if (textComponent.$validation) {
      return;
    }
    textComponent.$validation = {};

    let _origCheckValidity;
    let _origValidate;

    textComponent.$validation.disableClientValidation = function() {
      if (typeof _origCheckValidity === 'undefined') {
        _origCheckValidity = textComponent.checkValidity;
        textComponent.checkValidity = () => true;
      }
      if (typeof _origValidate === 'undefined') {
        _origValidate = textComponent.validate;
        textComponent.validate = () => true;
      }
    };

    textComponent.$validation.enableClientValidation = function() {
        if (_origCheckValidity) {
          textComponent.checkValidity = _origCheckValidity;
          _origCheckValidity = undefined;
        }
        if (_origValidate) {
          textComponent.validate = _origValidate;
          _origValidate = undefined;
        }
    }
  }
};
